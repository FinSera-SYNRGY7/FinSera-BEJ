package com.finalproject.finsera.finsera.dto.virtualAccount;

import lombok.Data;

@Data
public class AccountLastTransactionResponseDto {
    private String accountName;
    private String accountNumber;

    public AccountLastTransactionResponseDto(String accountName, String accountNumber) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }
}
