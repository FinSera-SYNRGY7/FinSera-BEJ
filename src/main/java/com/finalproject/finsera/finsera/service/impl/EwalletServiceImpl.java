package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.ewallet.*;
import com.finalproject.finsera.finsera.mapper.EwalletMapper;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.EwalletService;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class EwalletServiceImpl implements EwalletService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    EwalletAccountsRepository ewalletAccountsRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionNumberRepository transactionNumberRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EwalletRepository ewalletRepository;

    @Autowired
    EwalletMapper ewalletMapper;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public EwalletResponse createEwalletTransactions(long idCustomers, EwalletRequest ewalletRequest) {
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findById(idCustomers)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")));
        Optional<Ewallet> ewallet = Optional.ofNullable(ewalletRepository.findById(ewalletRequest.getEwalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "E-Wallet tidak ditemukan")));
        Optional<EwalletAccounts> ewalletAccounts = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAndEwalletAccountNumber(ewallet.get(), ewalletRequest.getEwalletAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor E-Wallet tidak ditemukan")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if(customers.get().getStatusUser() == StatusUser.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Akun anda terblokir");
        }
        if(bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor rekening tidak ditemukan");
        }


        int nominal = ewalletRequest.getNominal() + 2500;
        if (bankAccounts.getAmount() - nominal < 0) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Saldo Anda Tidak Cukup");
        }
        if (!(passwordEncoder.matches(ewalletRequest.getPin(), bankAccounts.getMpinAccount()))) {
            int newFailAttempts = bankAccounts.getFailedAttempt() + 1;
            bankAccounts.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(bankAccounts);
            if (bankAccounts.getFailedAttempt() > 3) {
                customers.get().setStatusUser(StatusUser.INACTIVE);
                customers.get().setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers.get());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun anda terblokir");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pin yang anda masukkan salah");
        } else {
            bankAccounts.setFailedAttempt(0);
            bankAccountsRepository.save(bankAccounts);
        }
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        TransactionsNumber transactionsNumberSaved = transactionNumberRepository.save(transactionsNumber);

        Transactions transactions = new Transactions();
        transactions.setBankAccounts(bankAccounts);
        transactions.setTransactionsNumber(transactionsNumberSaved);
        transactions.setFromAccountNumber(bankAccounts.getAccountNumber());
        transactions.setToAccountNumber(ewalletAccounts.get().getEwalletAccountNumber());
        transactions.setAmountTransfer((double) nominal);
        transactions.setNotes(ewalletRequest.getNote());
        transactions.setType(TransactionsType.TOP_UP_EWALLET);
        transactions.setTransactionInformation(TransactionInformation.UANG_KELUAR);


        bankAccounts.setAmount(bankAccounts.getAmount() - (double) nominal);
        ewalletAccounts.get().setAmount(ewalletAccounts.get().getAmount() + (double) nominal);
        bankAccountsRepository.save(bankAccounts);
        ewalletAccountsRepository.save(ewalletAccounts.get());
        Transactions transactionSaved = transactionRepository.save(transactions);
//        log.info("Transaction :" + transactionSaved);


        return ewalletMapper.toCreateTransactionEwalletResponse(ewalletAccounts,bankAccounts, ewalletRequest, transactionSaved);
    }



    @Override
    public EwalletCheckResponse checkAccountEwallet(EwalletCheckAccountRequest ewalletCheckAccountRequest) {

        Optional<Ewallet> ewallet = Optional.ofNullable(ewalletRepository.findById(ewalletCheckAccountRequest.getEwalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "E-wallet tidak ditemukkan")));
        Optional<EwalletAccounts> ewalletAccount = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAndEwalletAccountNumber(ewallet.get(), ewalletCheckAccountRequest.getEwalletAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor E-wallet tidak ditemukkan")));

        return ewalletMapper.toEwalletResponse(ewalletAccount.get());
    }

    @Override
    public List<GetAllEwalletResponse> getAllEwallet() {
        List<Ewallet> listEwallets = ewalletRepository.findAll();

        if(listEwallets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "E-Wallet tidak ditemukkan");
        }
        return ewalletMapper.toGetAllEwalletResponse(listEwallets);
    }

    @Override
    @Transactional
    public List<EwalletHistoryResponse> historyTransactionEwallet(long idCustomers){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(idCustomers);
        Optional<List<Transactions>> sender = transactionRepository.getAllHistoryByToAccountNumber(optionalBankAccountsSender.get(0).getAccountNumber(), TransactionsType.TOP_UP_EWALLET);
        if (sender.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan");
        }
        List<EwalletAccounts> historyList = new ArrayList<>();

        for (Transactions transaction : sender.get()) {
            Optional<EwalletAccounts> bankAccountsOtherBanks = ewalletAccountsRepository.findByEwalletAccountNumber(transaction.getToAccountNumber());
            historyList.add(bankAccountsOtherBanks.get());
        }

        return ewalletMapper.toGetAllHistoryEwalletResponse(historyList);
    }

}
