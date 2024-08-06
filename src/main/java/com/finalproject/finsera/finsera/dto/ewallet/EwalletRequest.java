package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Data;

@Data
public class EwalletRequest {
    private int ewalletId;
    private String ewalletAccount;
    private int nominal;
    private String note;
    private String pin;
}
