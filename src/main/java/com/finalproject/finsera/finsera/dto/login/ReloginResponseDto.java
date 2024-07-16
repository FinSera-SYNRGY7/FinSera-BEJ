package com.finalproject.finsera.finsera.dto.login;

import com.finalproject.finsera.finsera.model.enums.StatusUser;
import lombok.Data;

@Data
public class ReloginResponseDto {
    private String username;
    private StatusUser status;

    public ReloginResponseDto(String username, StatusUser status) {
        this.username = username;
        this.status = status;
    }
}
