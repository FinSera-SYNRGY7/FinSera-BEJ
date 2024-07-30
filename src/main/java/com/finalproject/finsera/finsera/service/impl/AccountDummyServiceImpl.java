package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.accountDummy.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.AccountType;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.AccountDummyRepository;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.AccountDummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDummyServiceImpl implements AccountDummyService {
    @Autowired
    AccountDummyRepository accountDummyRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public AccountDummyData createVirtualAccount(CreateAccountDummyRequestDto createAccountDummyRequestDto) {
        AccountDummyData account = new AccountDummyData();
        account.setAccountName(createAccountDummyRequestDto.getAccountName());
        account.setAccountNumber(createAccountDummyRequestDto.getAccountNumber());
        account.setAccountType(AccountType.VIRTUAL_ACCOUNT);
        account.setAmount(createAccountDummyRequestDto.getAmount());
        account.setMpinAccount(createAccountDummyRequestDto.getMpinAccount());
        account.setSavedAccount(true);
        accountDummyRepository.save(account);
        return account;
    }

    @Override
    public AccountDummyData checkAccount(String accountNum) {
        return accountDummyRepository.findByAccountNumber(accountNum);
    }

    @Override
    public BankAccounts checkBankAccount(Long id) {
        return bankAccountsRepository.findById(id).get();
    }

    @Override
    public List<Transactions> getAll() {
        List<Transactions> transactionsList = transactionRepository.findAll();
        transactionsList.forEach(transactions -> transactions.getType().equals(TransactionsType.VIRTUAL_ACCOUNT));
        return transactionsList;
    }

    @Override
    public List<AccountLastTransactionResponseDto> getAccount() {
        return transactionRepository.findByCreatedDate();
    }
}
