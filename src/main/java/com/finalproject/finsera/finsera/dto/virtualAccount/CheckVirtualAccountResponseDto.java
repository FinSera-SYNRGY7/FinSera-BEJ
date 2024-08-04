package com.finalproject.finsera.finsera.dto.virtualAccount;

import lombok.Data;

@Data
public class CheckVirtualAccountResponseDto {
    private String accountName;
    private String accountNum;
    private Double nominal;
}
