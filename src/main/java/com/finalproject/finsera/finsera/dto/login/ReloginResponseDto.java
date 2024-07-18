package com.finalproject.finsera.finsera.dto.login;

import com.finalproject.finsera.finsera.model.enums.StatusUser;
import lombok.Data;

@Data
public class ReloginResponseDto {
    private String token;
    private String mpin;

    public ReloginResponseDto(String token, String mpin) {
        this.token = token;
        this.mpin = mpin;
    }
}
