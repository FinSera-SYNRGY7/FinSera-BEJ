package com.finalproject.finsera.finsera.service;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionResponseDto;
import com.finalproject.finsera.finsera.dto.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.transferVirtualAccount.TransferVirtualAccountResponseDto;


public interface TransactionService {
    TransactionResponseDto create(TransactionRequestDto transactionRequestDto); 
    TransactionCheckAccountResponseDto check(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto);
    TransferVirtualAccountResponseDto transferVA(Long id, TransferVirtualAccountRequestDto transferVirtualAccountRequestDto);
}
