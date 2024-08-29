package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionOtherBankResponse {
    @Schema(name = "transaction_num", example = "8098977776186651895")
    private String transaction_num;
    @Schema(name = "transaction_date", example = "20 Agustus 2024 18:54 WIB")
    private String transaction_date;
    @Schema(name = "name_sender", example = "John Doe")
    private String name_sender;
    @Schema(name = "accountnum_sender", example = "123456789")
    private String accountnum_sender;
    @Schema(name = "bank_name", example = "Bank Rakyat Indonesia (BRI)")
    private String bank_name;
    @Schema(name = "name_recipient", example = "Fuad")
    private String name_recipient;
    @Schema(name = "accountnum_recipient", example = "789012345")
    private String accountnum_recipient;
    @Schema(name = "nominal", example = "Rp15.000,00")
    private String nominal;
    @Schema(name = "admin_fee", example = "Rp2.500,00")
    private String admin_fee;
    @Schema(name = "note", example = "Transfer-Inter")
    private String note;
}
