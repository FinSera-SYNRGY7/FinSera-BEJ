package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionOtherBankRequest {
    private int bank_id;
    private String accountnum_recipient;
    private int nominal;
    private String note;
    private String pin;
}
