package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.virtualAccount.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.AccountType;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    PasswordEncoder passwordEncoder;

    @Override
    public VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto) {
        VirtualAccounts account = new VirtualAccounts();
        account.setAccountName(createVirtualAccountRequestDto.getAccountName());
        account.setAccountNumber(createVirtualAccountRequestDto.getAccountNumber());
        account.setAccountType(AccountType.VIRTUAL_ACCOUNT);
        account.setAmount(createVirtualAccountRequestDto.getAmount());
        account.setMpinAccount(passwordEncoder.encode(createVirtualAccountRequestDto.getMpinAccount()));
        account.setSavedAccount(false);
        virtualAccountRepository.save(account);
        return account;
    }

    @Override
    public VirtualAccounts checkAccount(String accountNum) {
        return virtualAccountRepository.findByAccountNumber(accountNum);
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
    public List<Object> getAccount() {
        List<Transactions> transactionsList = transactionRepository.getLastAccountTransaction();
        VirtualAccounts virtualAccounts = virtualAccountRepository.findByAccountNumber(
                transactionsList.stream().map(Transactions::getTransactionsNumber).toString()
        );
        if (virtualAccounts == null){
            return Collections.singletonList(transactionsList.stream()
                    .map(transactions -> ResponseEntity.ofNullable(BaseResponse.failure(400, "account not found"))).toList());
        }

        Set<String> seenAccountNumbers = new HashSet<>();
        return Collections.singletonList(transactionsList.stream()
                .filter(transactions -> virtualAccountRepository.findByAccountNumber(transactions.getToAccountNumber()).getSavedAccount())
                .filter(transactions -> seenAccountNumbers.add(transactions.getToAccountNumber()))
                .map(transactions -> new AccountLastTransactionResponseDto(
                        virtualAccountRepository.findByAccountNumber(transactions.getToAccountNumber())
                                .getAccountName(),
                        transactions.getToAccountNumber()
                )).toList());
    }
}
