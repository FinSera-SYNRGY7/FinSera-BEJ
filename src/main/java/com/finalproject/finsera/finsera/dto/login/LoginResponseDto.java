package com.finalproject.finsera.finsera.dto.login;

import com.finalproject.finsera.finsera.model.enums.StatusUser;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private StatusUser status;

    public LoginResponseDto(String accessToken, String refreshToken, Long userId, StatusUser status) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.status = status;
    }
}
