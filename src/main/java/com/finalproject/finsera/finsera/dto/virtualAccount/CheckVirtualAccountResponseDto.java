package com.finalproject.finsera.finsera.dto.virtualAccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheckVirtualAccountResponseDto {

    @Schema(name = "accountNum", example = "123456789")
    private String accountNum;
    @Schema(name = "accountName", example = "BRIVA")
    private String accountName;
    @Schema(name = "nominal", example = "50000")
    private Double nominal;
}
