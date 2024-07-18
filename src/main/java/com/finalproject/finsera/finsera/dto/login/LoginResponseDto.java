package com.finalproject.finsera.finsera.dto.login;

import com.finalproject.finsera.finsera.model.enums.StatusUser;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String username;
    private StatusUser status;

    public LoginResponseDto(String token, String username, StatusUser status) {
        this.token = token;
        this.username = username;
        this.status = status;
    }
}
