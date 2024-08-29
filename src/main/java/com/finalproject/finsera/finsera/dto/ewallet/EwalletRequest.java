package com.finalproject.finsera.finsera.dto.ewallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EwalletRequest {

    @Schema(name = "ewalletId", example = "1")
    private long ewalletId;
    @Schema(name = "ewalletAccount", example = "089123123123")
    private String ewalletAccount;
    @Schema(name = "nominal", example = "10000")
    private int nominal;
    @Schema(name = "note", example = "top up")
    private String note;
    @Schema(name = "pin", example = "123456")
    private String pin;
}
