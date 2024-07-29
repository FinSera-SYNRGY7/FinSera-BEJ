package com.finalproject.finsera.finsera.dto.transaction;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private int id_user;
    private String accountnum_recipient;
    private int nominal;
    private String note;
    private String pin;
}
