package com.finalproject.finsera.finsera.dto.schemes.transactions;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletResponse;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsCreateExampleSwagger {
    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Transfer Berhasil")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private TransactionResponseDto data;
}
