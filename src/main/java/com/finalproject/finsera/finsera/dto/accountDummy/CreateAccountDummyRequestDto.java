package com.finalproject.finsera.finsera.dto.accountDummy;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import lombok.Data;

@Data
public class CreateAccountDummyRequestDto {
    private String accountName;
    private String accountNumber;
    private Double amount;
    private String mpinAccount;
    private AccountType accountType;
    private Boolean savedAccount;
}
