package com.finalproject.finsera.finsera.dto.schemes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotMpinExampleSwagger {
    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Update berhasil")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    @Schema(name = "data", example = "null")
    private Object data;
}
