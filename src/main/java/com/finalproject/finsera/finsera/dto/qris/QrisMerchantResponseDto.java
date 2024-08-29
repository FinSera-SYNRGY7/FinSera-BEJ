package com.finalproject.finsera.finsera.dto.qris;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrisMerchantResponseDto {

    @Schema(name = "transaction_num", example = "202408200931170001")
    private String transaction_num;
    @Schema(name = "transaction_date", example = "20 Agustus 2024 09:31 WIB")
    private String transaction_date;
    @Schema(name = "name_sender", example = "John Doe")
    private String name_sender;
    @Schema(name = "accountnum_sender", example = "123456789")
    private String accountnum_sender;
    @Schema(name = "name_recipient", example = "DOMPET DHUAFA ZAKAT")
    private String name_recipient;
    @Schema(name = "accountnum_recipient", example = "0215ID20200176137420303UME")
    private String accountnum_recipient;
    @Schema(name = "nominal", example = "Rp10.000,00")
    private String nominal;

}
