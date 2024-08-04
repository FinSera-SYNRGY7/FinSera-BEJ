package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginExampleSwagger {
    private String message;
    private LoginResponseDto data;
}
