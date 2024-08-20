package com.finalproject.finsera.finsera.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ForgotMpinRequestDto {
    @Schema(name = "password", example = "password123")
    private String password;
    @Schema(name = "newMpin", example = "654321")
    private String newMpin;
    @Schema(name = "confirmNewMpin", example = "654321")
    private String confirmNewMpin;
}
