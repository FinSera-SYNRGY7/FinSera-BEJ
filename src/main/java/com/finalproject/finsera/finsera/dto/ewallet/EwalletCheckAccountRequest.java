package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Data;

@Data
public class EwalletCheckAccountRequest {
    private long ewalletId;
    private String ewalletAccount;
}
