package com.finalproject.finsera.finsera.dto.ewallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletResponse {
    @Schema(name = "transactionNum", example = "202408201039130001")
    private String transactionNum;
    @Schema(name = "transactionDate", example = "20 Agustus 2024 10:39 WIB")
    private String transactionDate;
    @Schema(name = "nameSender", example = "John Doe")
    private String nameSender;
    @Schema(name = "accountSender", example = "123456789")
    private String accountSender;
    @Schema(name = "ewalletName", example = "DANA")
    private String ewalletName;
    @Schema(name = "ewalletAccountName", example = "Rahmat")
    private String ewalletAccountName;
    @Schema(name = "ewalletAccount", example = "089123123123")
    private String ewalletAccount;
    @Schema(name = "nominal", example = "Rp10.000,00")
    private String nominal;
    @Schema(name = "feeAdmin", example = "Rp2.500,00")
    private String feeAdmin;
    @Schema(name = "note", example = "top up")
    private String note;
}
