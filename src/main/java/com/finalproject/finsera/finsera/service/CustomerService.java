package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.login.*;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

public interface CustomerService {
    Customers register(RegisterRequestDto registerRequestDto);
    String getUserPin(String username);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
//    String relogin(String username, ReloginRequestDto reloginRequestDto);
    ResponseEntity<Map<String, Object>> relogin(Long id, ReloginRequestDto reloginRequestDto);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
    Customers updateMpin(String username, String newMpin);
}
