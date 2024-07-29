package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionCheckOtherBankResponse {
    private int bank_id;
    private String bank_name;
    private String accountnum_recipient;
    private String name_recipient;
    private int nominal;
    private String admin_fee;
    private String note;
}
