package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import lombok.Data;

@Data
public class TransferVirtualAccountExample {
    String transactionDate = "19 Agustus 2024 23:46 WIB";
    String transactionNum = "202408192346250001";
    String recipientName = "BRIVA";
    TransactionsType type = TransactionsType.valueOf("VIRTUAL_ACCOUNT");
    String recipientVirtualAccountNum = "081324510525005";
    String nominal = "100000";
}
