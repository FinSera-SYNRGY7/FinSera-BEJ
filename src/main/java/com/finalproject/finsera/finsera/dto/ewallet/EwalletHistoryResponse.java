package com.finalproject.finsera.finsera.dto.ewallet;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class EwalletHistoryResponse {

    @Schema(  name = "ewalletAccountId", example = "1" )
    private long ewalletAccountId;
    @Schema(name = "ewalletName", example = "DANA")
    private String ewalletName;
    @Schema(name = "ewalletImage", example = "https://storage.googleapis.com/image_bank_ewallet/ewallet/dana.png")
    private String ewalletImage;
    @Schema(name = "ewalletAccount", example = "089123123123")
    private String ewalletAccount;
    @Schema(name = "ewalletAccountName", example = "Rahmat")
    private String ewalletAccountName;

}
