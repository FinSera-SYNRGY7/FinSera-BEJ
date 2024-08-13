package com.finalproject.finsera.finsera.dto.virtualAccount;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import lombok.Data;

@Data
public class CreateVirtualAccountRequestDto {
    private String accountName;
    private String accountNumber;
    private Double nominal;
    private AccountType accountType;
}
