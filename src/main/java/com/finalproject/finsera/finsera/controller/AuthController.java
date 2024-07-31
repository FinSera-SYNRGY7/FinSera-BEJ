package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.BaseResponse;
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
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtUtilRefreshToken jwtUtilRefreshToken;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${security.jwt.secret-key}")
    private String secretKey;


    //ignore register service
    @PostMapping(value = {"user/register", "user/register/"})
    @Operation(summary = "Registrasi User")
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = RegisterExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequestDto registerRequestDto){
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        Map<String, Object> data = new HashMap<>();
        Customers registerCustomer = customerService.register(registerRequestDto);
        data.put("userDetails", registerCustomer);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/user/login", "/user/login/"})
    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = LoginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto){
        Optional<Customers> customer = Optional.ofNullable(customerRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "username or password invalid")
        ));
        if (customer.get().getStatusUser() == StatusUser.INACTIVE){
            return ResponseEntity.badRequest().body(BaseResponse.failure(400, "your account is inactive"));
        }

        if (!loginRequestDto.getUsername().equals(customer.get().getUsername())){
            return ResponseEntity.badRequest().body(BaseResponse.failure(400, "username or password invalid"));
        } else if (!passwordEncoder.matches(loginRequestDto.getPassword(), customer.get().getPassword())) {
            return ResponseEntity.badRequest().body(BaseResponse.failure(400, "username or password invalid"));
        } else {
            LoginResponseDto login = customerService.login(loginRequestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login success");
            response.put("data", login);
            return ResponseEntity.ok(BaseResponse.success(login, "login success"));
        }
    }

//    @PostMapping(value = {"/relogin", "/relogin/"})
//    @Operation(summary = "Relogin user", security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReloginExampleSwagger.class), mediaType = "application/json") })
//    public ResponseEntity<Map<String, Object>> relogin(@RequestHeader("Authorization") String token, @RequestBody ReloginRequestDto reloginRequestDto){
//        String jwt = token.substring("Bearer ".length());
//        String username = jwtUtil.getUsername(jwt);
//        String relogin = customerService.relogin(username, reloginRequestDto);
//        Customers customers = customerRepository.findByUsername(username).get();
//
//        if (customers.getMpin().equals(reloginRequestDto.getMpin())){
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Relogin Success");
//            response.put("data", relogin);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Pin is invalid");
//            response.put("data", null);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping(value = {"/relogin", "/relogin/"})
    @Operation(summary = "Relogin user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReloginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> reloginGetId(@RequestHeader("Authorization") String token, @RequestBody ReloginRequestDto reloginRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        System.out.println(userId);

        String relogin = customerService.relogin(userId, reloginRequestDto);
        Customers customers = customerRepository.findById(userId).get();

        if (passwordEncoder.matches(reloginRequestDto.getMpinAuth(), customers.getMpinAuth())){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Relogin Success");
            response.put("data", relogin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pin is invalid");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/user/refresh-token", "/user/refresh-token/"})
    public ResponseEntity<Object> relogin(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        String username = jwtUtilRefreshToken.getUsername(refreshTokenRequestDto.getRefreshToken());
        Customers customers = customerRepository.findByUsername(username).get();
        if (!customerRepository.existsById(customers.getIdCustomers())){
            return ResponseEntity.ok(BaseResponse.failure(400, "user not found"));
        } else if (customers.getStatusUser().equals(StatusUser.INACTIVE)) {
            return ResponseEntity.ok(BaseResponse.failure(400, "your account is inactive"));
        } else {
            RefreshTokenResponseDto refreshTokenResponseDto = customerService.refreshToken(refreshTokenRequestDto);
            return ResponseEntity.ok(BaseResponse.success(refreshTokenResponseDto, "accessToken"));
        }
    }
}
