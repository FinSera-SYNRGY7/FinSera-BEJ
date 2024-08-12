package com.finalproject.finsera.finsera.dto.customer;

import lombok.Data;

@Data
public class ForgotMpinRequestDto {
    private String password;
    private String newMpin;
    private String confirmNewMpin;
}
