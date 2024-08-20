package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;

import java.util.List;
import java.util.stream.Stream;

public interface VirtualAccountService {
    VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto);
    BankAccounts checkBankAccount(Long id);
    List<Transactions> getAll();
    Stream<Object> getLastTransactionAccountVA();
    CheckVirtualAccountResponseDto checkVirtualAccount(CheckVirtualAccountRequestDto checkVirtualAccountRequestDto);
    TransferVirtualAccountResponseDto transferVA(Long id,
                                                 TransferVirtualAccountRequestDto transferVirtualAccountRequestDto);
}
