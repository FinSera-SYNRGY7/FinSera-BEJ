package com.finalproject.finsera.finsera.dto;

import lombok.Data;

@Data
public class TransactionCheckAccountRequestDto {
    private String accountnum_recipient;
    private int nominal;
    private String note;
}
