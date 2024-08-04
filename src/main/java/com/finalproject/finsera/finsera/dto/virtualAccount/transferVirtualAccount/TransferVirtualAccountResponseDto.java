package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import lombok.Data;

import java.util.Date;

@Data
public class TransferVirtualAccountResponseDto {
    String transactionNum;
    TransactionsType type;
    String transactionDate;
    String senderName;
    String senderAccountNum;
    String recipientName;
    String recipientAccountNum;
    String nominal;
    String adminFee;
    String note;
}
