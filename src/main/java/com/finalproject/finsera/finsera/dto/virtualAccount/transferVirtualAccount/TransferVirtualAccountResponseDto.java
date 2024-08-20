package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferVirtualAccountResponseDto {

    @Schema(name = "transactionDate", example = "20 Agustus 2024 12:55 WIB")
    String transactionDate;
    @Schema(name = "transactionNum", example = "202408201255590002")
    String transactionNum;
    @Schema(name = "recipientName", example = "BRIVA")
    String recipientName;
    @Schema(name = "type", example = "VIRTUAL_ACCOUNT")
    TransactionsType type;
    @Schema(name = "recipientVirtualAccountNum", example = "123456789")
    String recipientVirtualAccountNum;
    @Schema(name = "nominal", example = "50000.0")
    String nominal;
}
