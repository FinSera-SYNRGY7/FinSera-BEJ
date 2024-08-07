package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.ewallet.*;
import com.finalproject.finsera.finsera.mapper.EwalletMapper;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.EwalletService;
import com.finalproject.finsera.finsera.util.InsufficientBalanceException;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan")));
        Optional<Ewallet> ewallet = Optional.ofNullable(ewalletRepository.findById(ewalletRequest.getEwalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet tidak ditemukkan")));
        Optional<EwalletAccounts> ewalletAccounts = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAndEwalletAccountNumber(ewallet.get(), ewalletRequest.getEwalletAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet account tidak ditemukan")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if(bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor rekening tidak ditemukan");
        }

        if (!(passwordEncoder.matches(ewalletRequest.getPin(), bankAccounts.getMpinAccount())))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin Anda Salah");
        }
        int nominal = ewalletRequest.getNominal();
        if (bankAccounts.getAmount() - nominal < 0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        TransactionsNumber transactionsNumberSaved = transactionNumberRepository.save(transactionsNumber);

        Transactions transactions = new Transactions();
        transactions.setBankAccounts(bankAccounts);
        transactions.setTransactionsNumber(transactionsNumberSaved);
        transactions.setFromAccountNumber(bankAccounts.getAccountNumber());
        transactions.setToAccountNumber(ewalletAccounts.get().getEwalletAccountNumber());
        transactions.setAmountTransfer((double) ewalletRequest.getNominal());
        transactions.setNotes(ewalletRequest.getNote());
        transactions.setType(TransactionsType.TOP_UP_EWALLET);
        transactions.setTransactionInformation(TransactionInformation.UANG_KELUAR);


        bankAccounts.setAmount(bankAccounts.getAmount() - (double) ewalletRequest.getNominal());
        ewalletAccounts.get().setAmount(ewalletAccounts.get().getAmount() + (double) ewalletRequest.getNominal());
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor e-wallet tidak ditemukkan")));

        return ewalletMapper.toEwalletResponse(ewalletAccount.get());
    }

    @Override
    public List<GetAllEwalletResponse> getAllEwallet() {
        List<Ewallet> listEwallets = ewalletRepository.findAll();

        if(listEwallets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet tidak ditemukkan");
        }
        return ewalletMapper.toGetAllEwalletResponse(listEwallets);
    }

}
