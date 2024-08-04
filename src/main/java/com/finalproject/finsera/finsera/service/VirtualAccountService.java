package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;

import java.util.List;

public interface VirtualAccountService {
    VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto);
    VirtualAccounts checkAccount(String accountNum);
    BankAccounts checkBankAccount(Long id);
    List<Transactions> getAll();
    List<Object> getAccount();

}
