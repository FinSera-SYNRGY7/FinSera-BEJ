package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionRequestDto {
    @Schema(name = "accountnum_recipient", example = "987654321")
    private String accountnum_recipient;
    @Schema(name = "nominal", example = "50000")
    private int nominal;
    @Schema(name = "note", example = "Transfer-Intra")
    private String note;
    @Schema(name = "pin", example = "123456")
    private String pin;
}
