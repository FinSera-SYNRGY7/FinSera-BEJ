package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoSaldoExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Nomor Rekening ditemukan")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private InfoSaldoResponse data;
}
