package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.jasper.TransactionsReportJasperDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiRequestDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.mapper.MutasiMapper;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.MutasiService;
import com.finalproject.finsera.finsera.service.ValidationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class MutasiServiceImpl implements MutasiService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    BankAccountsOtherBanksRepository bankAccountsOtherBanksRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionOtherBankRepository transactionOtherBankRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    MutasiMapper mutasiMapper;

    @Transactional
    @Override
    public List<MutasiResponseDto> getMutasi(
            String username,
            Timestamp startDate,
            Timestamp endDate,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(((page > 0) ? page - 1 : page), size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Transactions> filteredTransactions;

        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if (bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening tidak ditemukan");
        }
        if (bankAccounts.getCustomer() == customers.get()) {
            if(startDate != null && endDate != null) {
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDateWithPage(startDate, endDate, bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"));
            } else {
                filteredTransactions = transactionRepository.findAllByBankAccounts(bankAccounts, pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"));
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening tidak ditemukan");
        }


        assert filteredTransactions != null;
        if(filteredTransactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan");
        }
        return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList());
    }

    @Override
    public byte[] transactionsReport(String username, Timestamp startDate, Timestamp endDate) {
        List<TransactionsReportJasperDto> itemsTransactions = new ArrayList<>();
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")));
        Optional<List<Transactions>> transactions;
        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if(bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening tidak ditemukan");
        }
        if(startDate != null && endDate != null) {
            transactions = transactionRepository.findAllByBankAccountsAndCreatedDate(startDate, endDate, bankAccounts.getIdBankAccounts());
            if(transactions.get().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan");
            }
            for(var i = 0; i < transactions.get().size(); i++){
                toResponseJasperDto(itemsTransactions, transactions.get(), i);
            }
        } else {
            transactions = transactionRepository.findAllByBankAccountsOrderByCreatedDateDesc(bankAccounts);
            if(transactions.get().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan");
            }
            log.info("transactions 2: {}", transactions.get().size());
            for(var i = 0; i < transactions.get().size(); i++){
                toResponseJasperDto(itemsTransactions, transactions.get(), i);
            }

        }
        JasperReport jasperReport;
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File jasperFile = new File(tempDir, "Transactions_report.jasper");

            if (jasperFile.exists()) {
                if (!jasperFile.delete()) {
                    throw new RuntimeException("Gagal menghapus file jesper yang sudah ada");
                }
            }

            try (InputStream jrxmlStream = getClass().getClassLoader().getResourceAsStream("Transactions_report.jrxml")) {
                if (jrxmlStream == null) {
                    throw new RuntimeException("File Transactions_report.jrxml tidak ditemukan di classpath");
                }

                jasperReport = JasperCompileManager.compileReport(jrxmlStream);
                JRSaver.saveObject(jasperReport, jasperFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Gagal membaca resource: " + e.getMessage(), e);
            }
        } catch (JRException e) {
            throw new RuntimeException("JasperReports exception: " + e.getMessage(), e);
        }

        String norek = bankAccounts.getAccountNumber();
        String tf0 = "UANG_KELUAR";
        String tf1 = "UANG_MASUK";

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemsTransactions);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("norek", norek);
        parameters.put("uangKeluar", tf0);
        parameters.put("uangMasuk", tf1);
        parameters.put("tanggalAwal", startDate);
        parameters.put("tanggalAkhir", endDate);

        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;

    }

    private void toResponseJasperDto(List<TransactionsReportJasperDto> itemsTransactions, List<Transactions> transactions, int i) {
        TransactionsReportJasperDto transactionsReportJasperDto = new TransactionsReportJasperDto();
        transactionsReportJasperDto.setCreated_date(transactions.get(i).getCreatedDate());
        transactionsReportJasperDto.setNotes(transactions.get(i).getNotes());
        transactionsReportJasperDto.setTransaction_information(transactions.get(i).getTransactionInformation().ordinal());
        transactionsReportJasperDto.setAmount_transfer(transactions.get(i).getAmountTransfer());
        itemsTransactions.add(transactionsReportJasperDto);
    }


}
