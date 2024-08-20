package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionCheckAccountRequestDto {
    @Schema(name = "accountnum_recipient", example = "987654321")
    private String accountnum_recipient;
}
