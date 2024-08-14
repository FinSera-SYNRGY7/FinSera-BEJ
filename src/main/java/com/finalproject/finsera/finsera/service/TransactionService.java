package com.finalproject.finsera.finsera.service;

import java.util.*;
import com.finalproject.finsera.finsera.dto.transaction.*;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;

public interface TransactionService {
    TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto, long idCustomers);
    TransactionCheckAccountResponseDto checkAccountIntraBank(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto);
    TransactionOtherBankResponse placeTransactionsInterBank(TransactionOtherBankRequest transactionOtherBankRequest, long customerId);
    TransactionCheckOtherBankResponse checkAccountOtherBank(TransactionCheckOtherBankAccountRequest transactionCheckAccountRequestDto);
    List<?> historyTransaction(long idCustomers);
    List<?> historyTransactionInterBank(long idCustomers);
}
