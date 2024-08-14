package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import lombok.Data;

@Data
public class TransferVirtualAccountRequestDto {
    String virtualAccountNumber;
    String mpinAccount;
}
