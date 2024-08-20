package com.finalproject.finsera.finsera.dto.schemes.transactions;

import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckOtherBankResponse;
import com.finalproject.finsera.finsera.dto.transaction.TransactionOtherBankResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsInterCheckExampleSwagger {
    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Data Rekening tersedia")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private TransactionCheckOtherBankResponse data;
}
