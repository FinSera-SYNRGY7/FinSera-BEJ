package com.finalproject.finsera.finsera.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
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
@RequestMapping("auth")
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
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequestDto registerRequestDto){
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        Map<String, Object> data = new HashMap<>();
        Customers registerCustomer = customerService.register(registerRequestDto);
        data.put("userDetails", registerCustomer);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
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

    @PostMapping("/relogin")
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
}
