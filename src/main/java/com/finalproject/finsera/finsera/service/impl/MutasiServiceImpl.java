package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.jasper.TransactionsReportJasperDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiRequestDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.mapper.MutasiMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
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
    TransactionRepository transactionRepository;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if (bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Number not found");
        }
        if (bankAccounts.getCustomer() == customers.get()) {
            if(startDate != null && endDate != null) {
                log.info("start and end date: {}");
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDateWithPage(startDate, endDate, bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
            } else {
                log.info("else: {}");
                filteredTransactions = transactionRepository.findAllByBankAccounts(bankAccounts, pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Number not found");
        }


        assert filteredTransactions != null;
        if(filteredTransactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not found");
        } else if (filteredTransactions.stream().toList().size() > 10) {
            return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList().subList(0, 10));
        } else {
            return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList());
        }
    }

    @Override
    public byte[] transactionsReport(String username, Timestamp startDate, Timestamp endDate) {
        List<TransactionsReportJasperDto> itemsTransactions = new ArrayList<>();
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        List<Transactions> transactions = transactionRepository.findAllByBankAccountsAndCreatedDate(startDate, endDate, bankAccounts.getIdBankAccounts())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not found"));
        for(var i = transactions.size()-1; i >= 0; i--){
            TransactionsReportJasperDto transactionsReportJasperDto = new TransactionsReportJasperDto();
            transactionsReportJasperDto.setCreated_date(transactions.get(i).getCreatedDate());
            transactionsReportJasperDto.setNotes(transactions.get(i).getNotes());
            transactionsReportJasperDto.setTransaction_information(transactions.get(i).getTransactionInformation().ordinal());
            transactionsReportJasperDto.setAmount_transfer(transactions.get(i).getAmountTransfer());
            itemsTransactions.add(transactionsReportJasperDto);
        }
        JasperReport jasperReport;
//        try {
//            jasperReport = (JasperReport)
//                    JRLoader.loadObject(ResourceUtils.getFile("classpath:Transactions_report.jasper"));
//        } catch (FileNotFoundException | JRException e) {
//            try {
//                File file = ResourceUtils.getFile("classpath:Transactions_report.jrxml");
//                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//                JRSaver.saveObject(jasperReport, "Transactions_report.jasper");
//            } catch (FileNotFoundException | JRException ex) {
//                throw new RuntimeException(e);
//            }
//        }

        try {
            File reportDirectory = new File("src/main/resources/");
            if (!reportDirectory.exists()) {
                reportDirectory.mkdirs(); // Membuat folder jika belum ada
            }
            File jasperFile = new File(reportDirectory, "Transactions_report.jasper");
            if (jasperFile.exists()) {
                jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            } else {
                File file = ResourceUtils.getFile("classpath:Transactions_report.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, jasperFile.getAbsolutePath());
            }
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }

        String norek = bankAccounts.getAccountNumber();
        String tf0 = "UANG_KELUAR";
        String tf1 = "UANG_MASUK";

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemsTransactions);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("norek", norek);
        parameters.put("tanggalAwal", startDate);
        parameters.put("tanggalAkhir", endDate);
        parameters.put("uangKeluar", tf0);
        parameters.put("uangMasuk", tf1);

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


}
