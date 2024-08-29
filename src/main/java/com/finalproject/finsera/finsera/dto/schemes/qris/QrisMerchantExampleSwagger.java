package com.finalproject.finsera.finsera.dto.schemes.qris;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrisMerchantExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Transfer berhasil")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private QrisMerchantResponseDto data;
}
