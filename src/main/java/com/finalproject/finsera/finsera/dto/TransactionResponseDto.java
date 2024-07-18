package com.finalproject.finsera.finsera.dto;

import lombok.Data;

@Data
public class TransactionResponseDto {
    private long transaction_num;
    private String transaction_date;
    private String name_sender;
    private String accountnum_sender;
    private String name_recipient;
    private String accountnum_recipient;
    private String nominal;
    private String note;
}
