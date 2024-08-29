package com.finalproject.finsera.finsera.dto.qris;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QrisMerchantRequestDto {

    @Schema(name = "merchantNo", example = "0215ID20200176137420303UME")
    String merchantNo;
    @Schema(name = "merchantName", example = "DOMPET DHUAFA ZAKAT")
    String merchantName;
    @Schema(name = "nominal", example = "10000")
    int nominal;
    @Schema(name = "pin", example = "123456")
    String pin;

}
