package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.AccountType;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class VirtualAccountServiceImpl implements VirtualAccountService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionNumberRepository transactionNumberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto) {
        VirtualAccounts account = new VirtualAccounts();
        account.setAccountName(createVirtualAccountRequestDto.getAccountName());
        account.setVirtualAccountNumber(createVirtualAccountRequestDto.getAccountNumber());
        account.setAccountType(AccountType.VIRTUAL_ACCOUNT);
        account.setNominal(createVirtualAccountRequestDto.getNominal());
//        account.setSavedAccount(false);
        virtualAccountRepository.save(account);
        return account;
    }

    @Override
    public BankAccounts checkBankAccount(Long id) {
        return bankAccountsRepository.findById(id).get();
    }

    @Override
    public List<Transactions> getAll() {
        List<Transactions> transactionsList = transactionRepository.findAll();
        return transactionsList;
    }

//    @Override
//    public ResponseEntity<Map<String, Object>> getAccount() {
//        List<Transactions> transactionsList = transactionRepository.getLastAccountTransaction();
////        List<Transactions> savedAccountList = transactionsList.stream()
////                .filter(transactions -> virtualAccountRepository
////                        .findByVirtualAccountNumber(transactions.getToAccountNumber()).getSavedAccount())
////                .toList();
//
//        if (transactionsList.isEmpty()){
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "transaction not found");
//            response.put("data", null);
//            return ResponseEntity.badRequest().body(response);
//        } else if (savedAccountList.isEmpty()) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "transaction not found");
//            response.put("data", null);
//            return ResponseEntity.badRequest().body(response);
//        } else {
//            Set<String> seenAccountNumbers = new HashSet<>();
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "success");
//            response.put("data", transactionsList.stream()
////                    .filter(transactions -> !savedAccountList.isEmpty())
//                    .filter(transactions -> seenAccountNumbers.add(transactions.getToAccountNumber()))
//                    .map(transactions -> new AccountLastTransactionResponseDto(
//                            virtualAccountRepository.findByVirtualAccountNumber(transactions.getToAccountNumber())
//                                    .getAccountName(),
//                            transactions.getToAccountNumber()
//                    )).toList());
//            return ResponseEntity.ok(response);
//        }
//
//    }

    @Override
    public ResponseEntity<Map<String, Object>> checkVirtualAccount(CheckVirtualAccountRequestDto checkVirtualAccountRequestDto) {
        VirtualAccounts virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(checkVirtualAccountRequestDto.getVirtualAccountNumber());

        if (virtualAccounts == null){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Virtual Account not found");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            CheckVirtualAccountResponseDto responseVirtualAccount = new CheckVirtualAccountResponseDto();
            responseVirtualAccount.setAccountNum(virtualAccounts.getVirtualAccountNumber());
            responseVirtualAccount.setAccountName(virtualAccounts.getAccountName());
            responseVirtualAccount.setNominal(virtualAccounts.getNominal());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", responseVirtualAccount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> transferVA(Long id,
                                                        TransferVirtualAccountRequestDto transferVirtualAccountRequestDto) {
        Customers customers = customerRepository.findById(id).get();
        if (customers.getStatusUser().equals(StatusUser.INACTIVE)){
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "your account is inactive");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        BankAccounts senderBankAccount = bankAccountsRepository.findByCustomerId(id);
        VirtualAccounts virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(
                transferVirtualAccountRequestDto.getVirtualAccountNumber()
        );

        if (!passwordEncoder.matches(
                transferVirtualAccountRequestDto.getMpinAccount(),
                senderBankAccount.getMpinAccount()
        )) {
            int newFailAttempts = senderBankAccount.getFailedAttempt() + 1;
            senderBankAccount.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(senderBankAccount);
            if (senderBankAccount.getFailedAttempt() > 3){
                customers.setStatusUser(StatusUser.INACTIVE);
                customers.setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers);

                Map<String, Object> response = new HashMap<>();
                response.put("data", null);
                response.put("message", "Your account is banned");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Pin is Invalid");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            senderBankAccount.setFailedAttempt(0);
            bankAccountsRepository.save(senderBankAccount);

            TransactionsNumber transactionsNumber = new TransactionsNumber();
            transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
            transactionNumberRepository.save(transactionsNumber);
            Double adminFee = 2500.0;

            //sender transaction
            Transactions transferVirtualAccount = new Transactions();
            transferVirtualAccount.setTransactionInformation(TransactionInformation.UANG_KELUAR);
            transferVirtualAccount.setBankAccounts(senderBankAccount);
            transferVirtualAccount.setType(TransactionsType.VIRTUAL_ACCOUNT);
            transferVirtualAccount.setTransactionsNumber(transactionsNumber);
            transferVirtualAccount.setFromAccountNumber(senderBankAccount.getAccountNumber());
            transferVirtualAccount.setToAccountNumber(virtualAccounts.getVirtualAccountNumber());
            transferVirtualAccount.setAmountTransfer(virtualAccounts.getNominal());
            transactionRepository.save(transferVirtualAccount);

            //update sender amount
            senderBankAccount.setAmount(senderBankAccount.getAmount() - virtualAccounts.getNominal());
            bankAccountsRepository.save(senderBankAccount);

            TransferVirtualAccountResponseDto transferVAResponse = new TransferVirtualAccountResponseDto();
            transferVAResponse.setTransactionDate(DateFormatterIndonesia.dateFormatterIND(Date.from(Instant.now())));
            transferVAResponse.setTransactionNum(transactionsNumber.getTransactionNumber());
            transferVAResponse.setRecipientName(virtualAccounts.getAccountName());
            transferVAResponse.setType(TransactionsType.VIRTUAL_ACCOUNT);
            transferVAResponse.setRecipientVirtualAccountNum(transferVirtualAccountRequestDto.getVirtualAccountNumber());
            transferVAResponse.setNominal(transferVirtualAccount.getAmountTransfer().toString());
            transferVAResponse.setAdminFee(adminFee.toString());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", transferVAResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
