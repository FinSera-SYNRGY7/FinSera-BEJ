package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;

public interface AccountDummyService {
    AccountDummyData createVirtualAccount(CreateAccountDummyRequestDto createAccountDummyRequestDto);
    AccountDummyData checkAccount(String accountNum);
    BankAccounts checkBankAccount(Long id);
}
