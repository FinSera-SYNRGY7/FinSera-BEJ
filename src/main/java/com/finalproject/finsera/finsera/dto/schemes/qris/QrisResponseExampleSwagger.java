package com.finalproject.finsera.finsera.dto.schemes.qris;

import com.finalproject.finsera.finsera.dto.qris.QrisMerchantResponseDto;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrisResponseExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Berhasil mendapatkan data")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private QrisResponseDto data;
}
