package com.finalproject.finsera.finsera.service;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckOtherBankAccountRequest;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckOtherBankResponse;
import com.finalproject.finsera.finsera.dto.transaction.TransactionOtherBankRequest;
import com.finalproject.finsera.finsera.dto.transaction.TransactionOtherBankResponse;
import com.finalproject.finsera.finsera.dto.transaction.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;


public interface TransactionService {
    TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto); 
    TransactionCheckAccountResponseDto checkAccountIntraBank(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto);   
    TransactionOtherBankResponse placeTransactionsInterBank(TransactionOtherBankRequest transactionOtherBankRequest);
    TransactionCheckOtherBankResponse checkAccountOtherBank(TransactionCheckOtherBankAccountRequest transactionCheckAccountRequestDto);
}
