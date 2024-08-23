package com.finalproject.finsera.finsera.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank
    @Schema(name = "username", example = "johndoe")
    private String username;
    @NotBlank
    @Schema(name = "password", example = "password123")
    private String password;
}
