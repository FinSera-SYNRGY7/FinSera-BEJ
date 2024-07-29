package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionOtherBankResponse {
    private String transaction_num;
    private String transaction_date;
    private String name_sender;
    private String accountnum_sender;
    private String bank_name;
    private String name_recipient;
    private String accountnum_recipient;
    private String nominal;
    private String admin_fee;
    private String note;
}
