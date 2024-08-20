package com.finalproject.finsera.finsera.dto.ewallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EwalletCheckAccountRequest {
    @Schema(name = "ewalletId", example = "1")
    private long ewalletId;
    @Schema(name = "ewalletAccount", example = "089123123123")
    private String ewalletAccount;
}
