package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VirtualAccountService {
    VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto);
    BankAccounts checkBankAccount(Long id);
    List<Transactions> getAll();
    ResponseEntity<Map<String, Object>> getAccount();
    ResponseEntity<Map<String, Object>> checkVirtualAccount(CheckVirtualAccountRequestDto checkVirtualAccountRequestDto);
    TransferVirtualAccountResponseDto transferVA(Long id, String virtualAccountNumber,
                                                 String bankAccountNumber,
                                                 TransferVirtualAccountRequestDto transferVirtualAccountRequestDto);
}
