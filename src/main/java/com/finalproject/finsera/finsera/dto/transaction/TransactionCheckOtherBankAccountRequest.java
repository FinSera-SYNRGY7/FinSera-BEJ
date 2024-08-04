package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionCheckOtherBankAccountRequest {
    private int bank_id;
    private String accountnum_recipient;
}
