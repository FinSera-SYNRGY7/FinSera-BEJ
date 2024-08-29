package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.login.RefreshTokenResponseDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Berhasil mendapatkan accessToken")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private RefreshTokenResponseDto data;
}
