package com.finalproject.finsera.finsera.dto.virtualAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountLastTransactionResponseDto {
    private String accountName;
    private String accountNumber;

}
