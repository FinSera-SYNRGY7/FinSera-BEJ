package com.finalproject.finsera.finsera.service;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionResponseDto;


public interface TransactionService {
    TransactionResponseDto create(TransactionRequestDto transactionRequestDto); 
    TransactionCheckAccountResponseDto check(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto);   
}
