package com.finalproject.finsera.finsera.dto.schemes.ewallet;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletCheckResponse;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EwalletCheckExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Akun e-wallet ditemukan")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private EwalletCheckResponse data;
}
