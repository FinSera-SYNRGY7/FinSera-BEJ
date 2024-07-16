package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    //ignore register service
    @PostMapping("/register")
    public ResponseEntity<Customers> register(@RequestBody RegisterRequestDto registerRequestDto){
        Customers registerCustomer = customerService.register(registerRequestDto);
        return ResponseEntity.ok(registerCustomer);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Customers customers = customerRepository.findByUsername(userDetails.getUsername()).get();
        if (customers != null){
            customers.setStatusUser(StatusUser.ACTIVE);
            customerRepository.save(customers);
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto(
                jwt, userDetails.getUsername(), customers.getStatusUser()
        );

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/relogin")
    public String getUsername(Principal principal, @RequestParam String mpin){
        String username = principal.getName();
        String userPin = customerService.getUserPin(username);
        if (mpin.equals(userPin)){
            return "Welcome back " + username;
        } else return "mpin invalid";
    }

}
