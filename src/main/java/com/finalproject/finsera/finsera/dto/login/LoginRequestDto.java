package com.finalproject.finsera.finsera.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Schema(name = "username", example = "johndoe")
    private String username;
    @Schema(name = "password", example = "password123")
    private String password;
}
