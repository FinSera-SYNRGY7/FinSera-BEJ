package com.finalproject.finsera.finsera.dto.login;

import lombok.Data;

@Data
public class ReloginResponseDto {
    private String token;
    private String mpinAuth;

    public ReloginResponseDto(String token, String mpinAuth) {
        this.token = token;
        this.mpinAuth = mpinAuth;
    }
}
