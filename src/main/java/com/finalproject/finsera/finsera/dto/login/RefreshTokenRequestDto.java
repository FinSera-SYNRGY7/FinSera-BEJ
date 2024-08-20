package com.finalproject.finsera.finsera.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    @Schema(name = "refreshToken", example = "eyJhbGci")
    private String refreshToken;
}
