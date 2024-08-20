package com.finalproject.finsera.finsera.dto.ewallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletCheckResponse {

    @Schema(name = "ewalletName", example = "DANA")
    private String ewalletName;
    @Schema(name = "ewalletAccountId", example = "1")
    private long ewalletAccountId;
    @Schema(name = "ewalletAccount", example = "089123123123")
    private String ewalletAccount;
    @Schema(name = "ewalletAccountName", example = "Rahmat")
    private String ewalletAccountName;
}
