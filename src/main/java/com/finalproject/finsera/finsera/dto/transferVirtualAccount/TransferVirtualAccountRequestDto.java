package com.finalproject.finsera.finsera.dto.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import lombok.Data;

@Data
public class TransferVirtualAccountRequestDto {
    String recipientAccountNum;
    AccountType recipientAccountType;
    Double nominal;
    String note;
    String mpinAccount;
}
