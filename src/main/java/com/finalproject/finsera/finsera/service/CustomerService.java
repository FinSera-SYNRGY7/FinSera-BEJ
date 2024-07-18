package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

public interface CustomerService {
    Customers register(RegisterRequestDto registerRequestDto);
    String getUserPin(String username);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    String relogin(String username, ReloginRequestDto reloginRequestDto);
}
