package com.finalproject.finsera.finsera.dto;

import lombok.Data;

@Data
public class TransactionCheckAccountResponseDto {
    private String name_recipient;
    private String accountnum_recipient;
    private String nominal;
    private String note;
}
