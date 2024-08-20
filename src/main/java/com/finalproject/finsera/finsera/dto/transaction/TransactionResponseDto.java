package com.finalproject.finsera.finsera.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionResponseDto {
    @Schema(name = "transaction_num", example = "7440043808213725410")
    private String transaction_num;
    @Schema(name = "transaction_date", example = "20 Agustus 2024 14:46 WIB")
    private String transaction_date;
    @Schema(name = "name_sender", example = "John Doe")
    private String name_sender;
    @Schema(name = "accountnum_sender", example = "123456789")
    private String accountnum_sender;
    @Schema(name = "name_recipient", example = "Jane Smith")
    private String name_recipient;
    @Schema(name = "accountnum_recipient", example = "987654321")
    private String accountnum_recipient;
    @Schema(name = "nominal", example = "Rp50.000,00")
    private String nominal;
    @Schema(name = "note", example = "Transfer-Intra")
    private String note;
}
