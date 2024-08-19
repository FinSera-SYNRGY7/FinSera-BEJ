package com.finalproject.finsera.finsera.dto.qris;

import lombok.Data;

@Data
public class QrisMerchantRequestDto {

    String merchantNo;
    String merchantName;
    int nominal;
    String pin;

}
