package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import lombok.Data;

@Data
public class TransferVirtualAccountResponseDto {
    String transactionDate;
    String transactionNum;
    String recipientName;
    TransactionsType type;
    String recipientVirtualAccountNum;
    String nominal;
    String adminFee;
}
