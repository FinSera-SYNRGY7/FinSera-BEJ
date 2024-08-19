package com.finalproject.finsera.finsera.controller;

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
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.JwtUtilRefreshToken;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Registrasi User (done)")
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
    @Operation(summary = "Login user (done)")
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = LoginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto){
         LoginResponseDto login = customerService.login(loginRequestDto);
         Map<String, Object> response = new HashMap<>();
         response.put("message", "Login sukses");
         response.put("data", login);
         return ResponseEntity.ok(BaseResponse.success(login, "Login sukses"));
    }

    @PostMapping(value = {"/relogin", "/relogin/"})
    @Operation(summary = "Relogin user (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReloginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> reloginGetId(@RequestHeader("Authorization") String token, @RequestBody ReloginRequestDto reloginRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        return customerService.relogin(userId, reloginRequestDto);
    }

    @PostMapping(value = {"/user/refresh-token", "/user/refresh-token/"})
    @Operation(summary = "Refresh Token (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = RefreshTokenResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Object> relogin(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        RefreshTokenResponseDto response = customerService.refreshToken(refreshTokenRequestDto);
        return ResponseEntity.ok(BaseResponse.success(response, "accessToken"));
    }

    @PostMapping(value = {"/user/forgot-mpin", "/user/forgot-mpin/"})
    @Operation(summary = "Forgot Mpin", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> forgotMpin(@RequestHeader("Authorization") String token, @RequestBody ForgotMpinRequestDto forgotMpinRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        return customerService.forgotMpin(userId, forgotMpinRequestDto);
    }
}
