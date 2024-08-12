package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import lombok.Data;

@Data
public class TransferVirtualAccountRequestDto {
//    String recipientVirtualAccountNum;
//    AccountType recipientAccountType;
    Double nominal;
//    String note;
    String mpinAccount;
    Boolean saveAccount;
}
