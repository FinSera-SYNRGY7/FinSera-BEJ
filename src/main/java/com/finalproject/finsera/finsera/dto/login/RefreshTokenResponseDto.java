package com.finalproject.finsera.finsera.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenResponseDto {

    @Schema(name = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMzUyMDJ9.VnUPW32phiQvPwqK8RrO0wHp8Dfh5p9kuyEXXVYoePU")
    private String accessToken;
}
