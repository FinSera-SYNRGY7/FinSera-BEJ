package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.accountDummy.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;

import java.util.List;

public interface AccountDummyService {
    AccountDummyData createVirtualAccount(CreateAccountDummyRequestDto createAccountDummyRequestDto);
    AccountDummyData checkAccount(String accountNum);
    BankAccounts checkBankAccount(Long id);
    List<Transactions> getAll();
    List<AccountLastTransactionResponseDto> getAccount();

}
