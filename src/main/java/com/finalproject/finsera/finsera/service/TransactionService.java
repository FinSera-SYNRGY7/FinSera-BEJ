package com.finalproject.finsera.finsera.service;


import com.finalproject.finsera.finsera.dto.transaction.*;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;

public interface TransactionService {
    TransferVirtualAccountResponseDto transferVA(Long id, TransferVirtualAccountRequestDto transferVirtualAccountRequestDto);
    TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto);
    TransactionCheckAccountResponseDto checkAccountIntraBank(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto);
    TransactionOtherBankResponse placeTransactionsInterBank(TransactionOtherBankRequest transactionOtherBankRequest);
    TransactionCheckOtherBankResponse checkAccountOtherBank(TransactionCheckOtherBankAccountRequest transactionCheckAccountRequestDto);
}
