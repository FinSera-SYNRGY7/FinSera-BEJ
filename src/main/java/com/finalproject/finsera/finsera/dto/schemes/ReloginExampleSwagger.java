package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReloginExampleSwagger {
    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Relogin sukses")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    @Schema(name = "data", example = "null")
    private Object data;
}
