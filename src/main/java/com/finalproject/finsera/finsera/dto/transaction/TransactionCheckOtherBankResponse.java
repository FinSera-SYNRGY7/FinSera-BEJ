package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionCheckOtherBankResponse {
    @Schema(name = "bank_id", example = "2")
    private int bank_id;
    @Schema(name = "bank_name", example = "Bank Rakyat Indonesia (BRI)")
    private String bank_name;
    @Schema(name = "accountnum_recipient", example = "789012345")
    private String accountnum_recipient;
    @Schema(name = "name_recipient", example = "Ahmad")
    private String name_recipient;
}
