package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionCheckAccountResponseDto {
    private String name_recipient;
    private String accountnum_recipient;
    private int nominal;
    private String note;
}
