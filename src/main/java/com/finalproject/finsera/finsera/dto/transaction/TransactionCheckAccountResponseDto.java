package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionCheckAccountResponseDto {
    @Schema(name = "name_recipient", example = "Jane Smith")
    private String name_recipient;
    @Schema(name = "accountnum_recipient", example = "987654321")
    private String accountnum_recipient;
}
