package com.finalproject.finsera.finsera.dto.login;

import com.finalproject.finsera.finsera.model.enums.StatusUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginResponseDto {
    @Schema(name = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMzY2MzIsImV4cCI6MTcyNDEzODQzMn0.-SWz1VGOsEtR1BYNUsyDZ1iG3HRr2Byi9g4yuQfiLwk")
    private String accessToken;
    @Schema(name = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMzY2MzJ9.iQvkWV050HepCaO6Sy6fqas6biOmy-_senxC2vJ2eH41")
    private String refreshToken;
    @Schema(name = "userId", example = "1")
    private Long userId;
    @Schema(name = "status", example = "ACTIVE")
    private StatusUser status;

    public LoginResponseDto(String accessToken, String refreshToken, Long userId, StatusUser status) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.status = status;
    }
}
