package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.virtualAccount.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.TransactionsNumber;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.AccountType;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.TransactionNumberRepository;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
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
        account.setMpinAccount(passwordEncoder.encode(createVirtualAccountRequestDto.getMpinAccount()));
        account.setSavedAccount(false);
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

    @Override
    public ResponseEntity<Map<String, Object>> getAccount() {
        List<Transactions> transactionsList = transactionRepository.getLastAccountTransaction();
        List<Transactions> savedAccountList = transactionsList.stream()
                .filter(transactions -> virtualAccountRepository
                        .findByVirtualAccountNumber(transactions.getToAccountNumber()).get().getSavedAccount())
                .toList();

        if (transactionsList.isEmpty()){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "transaction not found");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        } else if (savedAccountList.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "transaction not found");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        } else {
            Set<String> seenAccountNumbers = new HashSet<>();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", transactionsList.stream()
//                    .filter(transactions -> !savedAccountList.isEmpty())
                    .filter(transactions -> seenAccountNumbers.add(transactions.getToAccountNumber()))
                    .map(transactions -> new AccountLastTransactionResponseDto(
                            virtualAccountRepository.findByVirtualAccountNumber(transactions.getToAccountNumber())
                                    .get().getAccountName(),
                            transactions.getToAccountNumber()
                    )).toList());
            return ResponseEntity.ok(response);
        }

    }

    @Override
    public ResponseEntity<Map<String, Object>> checkVirtualAccount(CheckVirtualAccountRequestDto checkVirtualAccountRequestDto) {
        Optional<VirtualAccounts> virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(checkVirtualAccountRequestDto.getVirtualAccountNumber());
        if (virtualAccounts.isPresent()){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", virtualAccounts.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", "null");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @Override
    public TransferVirtualAccountResponseDto transferVA(Long id, String virtualAccountNumber,
                                                        String bankAccountNumber,
                                                        TransferVirtualAccountRequestDto transferVirtualAccountRequestDto) {
        BankAccounts senderBankAccount = bankAccountsRepository.findByCustomerId(id);
        Optional<BankAccounts> optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber(bankAccountNumber);
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();
        VirtualAccounts recipientVirtualAccount = virtualAccountRepository.findByVirtualAccountNumber(virtualAccountNumber).get();

        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        transactionNumberRepository.save(transactionsNumber);
        Double adminFee = 2500.0;

        //sender transaction
        Transactions senderTransaction = new Transactions();
        senderTransaction.setTransactionInformation(TransactionInformation.UANG_KELUAR);
        senderTransaction.setBankAccounts(senderBankAccount);
        senderTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);
        senderTransaction.setTransactionsNumber(transactionsNumber);
        senderTransaction.setFromAccountNumber(senderBankAccount.getAccountNumber());
        senderTransaction.setToAccountNumber(recipientVirtualAccount.getVirtualAccountNumber());
        senderTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());
        transactionRepository.save(senderTransaction);

        //sender update amount
        senderBankAccount.setAmount(senderBankAccount.getAmount() - transferVirtualAccountRequestDto.getNominal() - adminFee);
        bankAccountsRepository.save(senderBankAccount);

        //recipient transaction
        Transactions recipientTransaction = new Transactions();
        recipientTransaction.setTransactionInformation(TransactionInformation.UANG_MASUK);
        recipientTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);

        //TBD should be same with noTransaction of sender or not
        recipientTransaction.setTransactionsNumber(transactionsNumber);
        recipientTransaction.setBankAccounts(bankAccountsReceiver);
        recipientTransaction.setFromAccountNumber(recipientVirtualAccount.getVirtualAccountNumber());
        recipientTransaction.setToAccountNumber(senderBankAccount.getAccountNumber());
        recipientTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());
        transactionRepository.save(recipientTransaction);

        //recipient update amount and save
        recipientVirtualAccount.setNominal(
                recipientVirtualAccount.getNominal() + transferVirtualAccountRequestDto.getNominal()
        );
        recipientVirtualAccount.setSavedAccount(transferVirtualAccountRequestDto.getSaveAccount());
        virtualAccountRepository.save(recipientVirtualAccount);

        TransferVirtualAccountResponseDto response = new TransferVirtualAccountResponseDto();
        response.setTransactionDate(DateFormatterIndonesia.dateFormatterIND(Date.from(Instant.now())));
        response.setTransactionNum(transactionsNumber.getTransactionNumber());

        return response;
    }
}
