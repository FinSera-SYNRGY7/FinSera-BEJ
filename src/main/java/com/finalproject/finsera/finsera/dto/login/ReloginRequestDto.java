package com.finalproject.finsera.finsera.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReloginRequestDto {
    @Schema(name = "mpinAuth", example = "123456")
    private String mpinAuth;
}
