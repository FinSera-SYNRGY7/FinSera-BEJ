package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.qris.QrisMerchantRequestDto;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantResponseDto;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;
import com.finalproject.finsera.finsera.mapper.QrisMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.entity.TransactionsNumber;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.repository.TransactionNumberRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.QrisService;
import com.finalproject.finsera.finsera.util.InsufficientBalanceException;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QrisServiceImpl implements QrisService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TransactionNumberRepository transactionNumberRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    QrisMapper qrisMapper;


    @Override
    public QrisResponseDto generateQris(String username) {
        Optional<Customers> optionalCustomer = customerRepository.findByUsername(username);

        if (optionalCustomer.isPresent()) {
            Customers customers = optionalCustomer.get();
            QrisResponseDto response = new QrisResponseDto();
            response.setAccountNumber(customers.getBankAccounts().get(0).getAccountNumber());
            response.setUsername(customers.getUsername());
            return response;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        }
    }

    @Override
    public QrisMerchantResponseDto merchnatQris(long customerId, QrisMerchantRequestDto qrisMerchantRequestDto) {
        Customers customers = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));
        BankAccounts bankAccountsSender = bankAccountsRepository.findByCustomerId(customerId);
        if (bankAccountsSender.getAmount() - qrisMerchantRequestDto.getNominal() < 0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }
        if (!(passwordEncoder.matches(qrisMerchantRequestDto.getPin(), bankAccountsSender.getMpinAccount()))) {
            int newFailAttempts = bankAccountsSender.getFailedAttempt() + 1;
            bankAccountsSender.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(bankAccountsSender);
            if (bankAccountsSender.getFailedAttempt() > 3) {
                customers.setStatusUser(StatusUser.INACTIVE);
                customers.setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Akun anda terblokir");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pin yang anda masukkan salah");
        } else {
            bankAccountsSender.setFailedAttempt(0);
            bankAccountsRepository.save(bankAccountsSender);
        }
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        TransactionsNumber transactionsNumberSaved =  transactionNumberRepository.save(transactionsNumber);

        Transactions transaction_out = new Transactions();
        transaction_out.setBankAccounts(bankAccountsSender);
        transaction_out.setTransactionsNumber(transactionsNumberSaved);
        transaction_out.setFromAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_out.setToAccountNumber(qrisMerchantRequestDto.getMerchantNo());
        transaction_out.setAmountTransfer((double) qrisMerchantRequestDto.getNominal());
        transaction_out.setNotes(null);
        transaction_out.setType(TransactionsType.QRIS);
        transaction_out.setTransactionInformation(TransactionInformation.UANG_KELUAR);

        bankAccountsSender.setAmount(bankAccountsSender.getAmount()-(double) qrisMerchantRequestDto.getNominal());
        bankAccountsRepository.save(bankAccountsSender);
        Transactions transactionsaved = transactionRepository.save(transaction_out);
        transactionRepository.save(transaction_out);

        return qrisMapper.toCreateTransactionQrisResponse(bankAccountsSender, qrisMerchantRequestDto, transactionsaved);
    }
}
