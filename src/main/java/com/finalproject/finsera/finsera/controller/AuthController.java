package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.exception.ErrorResponse;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    ErrorResponse errorResponse;

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
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<Customers> optionalCustomers = customerRepository.findByUsername(userDetails.getUsername());
            Customers customers = optionalCustomers.get();
            if (optionalCustomers.isPresent()
                    &&
                    loginRequestDto.getUsername().equals(customers.getUsername())
                    &&
                    passwordEncoder.matches(loginRequestDto.getPassword(), customers.getPassword())
            ){
                customers.setStatusUser(StatusUser.ACTIVE);
                customerRepository.save(customers);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login success");

                LoginResponseDto loginResponseDto = new LoginResponseDto(
                        jwt, userDetails.getUsername(), customers.getStatusUser()
                );
                response.put("data", loginResponseDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                System.out.println(errorResponse);
                return errorResponse.message("Username or Password is invalid");
            }
        } catch (AuthenticationException e){
            System.out.println(e);
            return errorResponse.message("Username or Password is invalid");
        }

    }

    @PostMapping("/relogin")
    public ResponseEntity<Map<String, Object>> relogin(Principal principal, @RequestParam String mpin){
        String username = principal.getName();
        String userPin = customerService.getUserPin(username);
        Customers customers = customerRepository.findByUsername(username).get();
        if (mpin.equals(userPin)){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login success");

            ReloginResponseDto reloginResponseDto = new ReloginResponseDto(
                    customers.getUsername(), customers.getStatusUser()
            );
            response.put("data", reloginResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid Pin");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
