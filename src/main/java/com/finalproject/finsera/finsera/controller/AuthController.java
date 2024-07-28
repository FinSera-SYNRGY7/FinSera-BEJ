package com.finalproject.finsera.finsera.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.LoginExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.RegisterExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ReloginExampleSwagger;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto){
        Optional<Customers> customers = customerRepository.findByUsername(loginRequestDto.getUsername());
        if (customers.isPresent()){
            LoginResponseDto login = customerService.login(loginRequestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login success");
            response.put("data", login);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Username or Password is invalid");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = {"/relogin", "/relogin/"})
    @Operation(summary = "Relogin user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReloginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> relogin(@RequestHeader("Authorization") String token, @RequestBody ReloginRequestDto reloginRequestDto){
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        String relogin = customerService.relogin(username, reloginRequestDto);
        Customers customers = customerRepository.findByUsername(username).get();

        if (customers.getMpin().equals(reloginRequestDto.getMpin())){
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

    @PostMapping(value = {"/relogin-get-id", "/relogin-get-id/"})
    @Operation(summary = "Relogin user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReloginExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> reloginGetId(@RequestHeader("Authorization") String token, @RequestBody ReloginRequestDto reloginRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        System.out.println(userId);

        String relogin = customerService.reloginGetId(userId, reloginRequestDto);
        Customers customers = customerRepository.findById(userId).get();

        if (passwordEncoder.matches(reloginRequestDto.getMpin(), customers.getMpin())){
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
}
