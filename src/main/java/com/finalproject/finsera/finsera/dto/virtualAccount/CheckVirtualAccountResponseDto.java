package com.finalproject.finsera.finsera.dto.virtualAccount;

import lombok.Data;

@Data
public class CheckVirtualAccountResponseDto {
    private String accountNum;
    private String accountName;
    private Double nominal;
}
