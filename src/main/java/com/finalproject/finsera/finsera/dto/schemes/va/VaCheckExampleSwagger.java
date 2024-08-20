package com.finalproject.finsera.finsera.dto.schemes.va;

import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountResponseDto;
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
public class VaCheckExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Nomor Virtual Account ditemukan")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private CheckVirtualAccountResponseDto data;
}
