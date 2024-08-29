package com.finalproject.finsera.finsera.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.customer.ForgotMpinRequestDto;
import com.finalproject.finsera.finsera.dto.login.*;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.dto.schemes.LoginExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.RegisterExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ReloginExampleSwagger;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.ApiResponseAnnotations;
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.JwtUtilRefreshToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "API untuk operasi authentication")
public class AuthController {
    @Autowired
    CustomerService customerService;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    //ignore register service
    @PostMapping(value = {"user/register", "user/register/"})
    @Operation(summary = "Registrasi User (internal)")
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = RegisterExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequestDto registerRequestDto){
        Map<String, Object> response = new HashMap<>();
        response.put("status", "sukses");

        Map<String, Object> data = new HashMap<>();
        Customers registerCustomer = customerService.register(registerRequestDto);
        data.put("userDetails", registerCustomer);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/user/login", "/user/login/"})
    @ApiResponseAnnotations.LoginResponses
    @Operation(summary = "Login user (done)")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto){
         LoginResponseDto login = customerService.login(loginRequestDto);
         Map<String, Object> response = new HashMap<>();
         response.put("message", "Login sukses");
         response.put("data", login);
         return ResponseEntity.ok(BaseResponse.success(login, "Login sukses"));
    }

    @PostMapping(value = {"/relogin", "/relogin/"})
    @ApiResponseAnnotations.ReloginResponses
    @Operation(summary = "Relogin user (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> reloginGetId(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader("Authorization") String token,
            @RequestBody ReloginRequestDto reloginRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        return ResponseEntity.ok(BaseResponse.success(customerService.relogin(userId, reloginRequestDto), "Relogin sukses"));
    }

    @PostMapping(value = {"/user/refresh-token", "/user/refresh-token/"})
    @ApiResponseAnnotations.RefreshTokenApiResponses
    @Operation(summary = "Refresh Token (done)")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        RefreshTokenResponseDto response = customerService.refreshToken(refreshTokenRequestDto);
        return ResponseEntity.ok(BaseResponse.success(response, "Berhasil mendapatkan accessToken"));
    }

    @PostMapping(value = {"/user/forgot-mpin", "/user/forgot-mpin/"})
    @ApiResponseAnnotations.ForgotMpinResponses
    @Operation(summary = "Forgot Mpin (internal)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> forgotMpin(@RequestHeader("Authorization") String token, @RequestBody ForgotMpinRequestDto forgotMpinRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        return ResponseEntity.ok(BaseResponse.success(customerService.forgotMpin(userId, forgotMpinRequestDto), "Update berhasil"));
    }
}
