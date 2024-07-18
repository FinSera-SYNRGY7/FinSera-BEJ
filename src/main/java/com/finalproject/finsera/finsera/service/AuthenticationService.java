package com.finalproject.finsera.finsera.service;


import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;

public interface AuthenticationService {

    LoginResponseDto login(LoginRequestDto request);

}