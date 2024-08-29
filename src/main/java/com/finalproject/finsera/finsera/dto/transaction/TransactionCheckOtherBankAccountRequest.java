package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionCheckOtherBankAccountRequest {

    @Schema(name = "bank_id", example = "2")
    private int bank_id;
    @Schema(name = "accountnum_recipient", example = "789012345")
    private String accountnum_recipient;
}
