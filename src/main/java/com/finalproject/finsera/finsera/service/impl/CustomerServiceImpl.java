package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.customer.ForgotMpinRequestDto;
import com.finalproject.finsera.finsera.dto.login.*;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.JwtUtilRefreshToken;
import com.finalproject.finsera.finsera.util.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtUtilRefreshToken jwtUtilRefreshToken;


    //ignore register service
    @Override
    public Customers register(RegisterRequestDto registerRequestDto) {
        Customers customers = new Customers();
        customers.setName(registerRequestDto.getName());
        customers.setNik(registerRequestDto.getNik());
        customers.setAddress(registerRequestDto.getAddress());

        if (registerRequestDto.getGender().equalsIgnoreCase("laki laki")
            || registerRequestDto.getGender().equalsIgnoreCase("laki-laki")
        ){
            customers.setGender(Gender.LAKI_LAKI);
        } else if (registerRequestDto.getGender().equalsIgnoreCase("perempuan")) {
            customers.setGender(Gender.PEREMPUAN);
        }

        customers.setFatherName(registerRequestDto.getFatherName());
        customers.setMotherName(registerRequestDto.getMotherName());
        customers.setPhoneNumber(registerRequestDto.getPhoneNumber());
        customers.setIncome(registerRequestDto.getIncome());
        customers.setUsername(registerRequestDto.getUsername());
        customers.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        customers.setEmail(registerRequestDto.getEmail());
        customers.setMpinAuth(passwordEncoder.encode(registerRequestDto.getMpinAuth()));
        customers.setStatusUser(StatusUser.ACTIVE);

        customerRepository.save(customers);
        return customers;
    }

    @Override
    public String getUserPin(String username) {
        Customers customers = customerRepository.findByUsername(username).get();
        return customers.getMpinAuth();
    }

    @Override
    public Customers updateMpin(String username, String newMpin){
        Optional<Customers> optionalCustomers = customerRepository.findByUsername(username);

        if (!optionalCustomers.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found");
        }
        Customers customer = optionalCustomers.get();
        customer.setMpinAuth(passwordEncoder.encode(newMpin));

        customerRepository.save(customer);
        return customer;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<Customers> optionalCustomers = customerRepository.findByUsername(loginRequestDto.getUsername());
        Customers customers = optionalCustomers.get();
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);
        String refreshToken = jwtUtilRefreshToken.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        customers.setStatusUser(StatusUser.ACTIVE);
        customerRepository.save(customers);
        LoginResponseDto loginResponseDto = new LoginResponseDto(
                    token, refreshToken, userDetails.getIdCustomers(), customers.getStatusUser()
            );

        return loginResponseDto;
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        //TODO validate present or not
        //TODO validate active or inactive
        String username = jwtUtilRefreshToken.getUsername(refreshTokenRequestDto.getRefreshToken());
        String token = jwtUtil.generateTokenFromUsername(username);
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        refreshTokenResponseDto.setAccessToken(token);
        return refreshTokenResponseDto;
    }

    @Override
    public ResponseEntity<Map<String, Object>> relogin(Long id, ReloginRequestDto reloginRequestDto) {
        Customers customers = customerRepository.findById(id).get();
        if (customers.getStatusUser().equals(StatusUser.INACTIVE)){
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "your account is inactive");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(reloginRequestDto.getMpinAuth(), customers.getMpinAuth())) {
            int newFailAttempts = customers.getFailedAttempt() + 1;
            customers.setFailedAttempt(newFailAttempts);
            customerRepository.save(customers);
            if (customers.getFailedAttempt() > 3) {
                customers.setStatusUser(StatusUser.INACTIVE);
                customers.setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers);

                Map<String, Object> response = new HashMap<>();
                response.put("data", null);
                response.put("message", "Your account is banned, due to invalid pin more than 3 times");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Pin is invalid");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            customers.setFailedAttempt(0);
            customers.setBannedTime(null);
            customerRepository.save(customers);
            Map<String, Object> response = new HashMap<>();
            response.put("data", "Pin Valid");
            response.put("message", "Relogin Success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> forgotMpin(Long id, ForgotMpinRequestDto forgotMpinRequestDto) {
        Customers customers = customerRepository.findById(id).get();

        if (!passwordEncoder.matches(forgotMpinRequestDto.getPassword(), customers.getPassword())){
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Password incorrect");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (!forgotMpinRequestDto.getNewMpin().equalsIgnoreCase(forgotMpinRequestDto.getConfirmNewMpin())) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "MPin not valid");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            customers.setPassword(passwordEncoder.encode(forgotMpinRequestDto.getPassword()));
            customers.setMpinAuth(passwordEncoder.encode(forgotMpinRequestDto.getNewMpin()));
            customerRepository.save(customers);

            Map<String, Object> response = new HashMap<>();
            response.put("data", "Your pin is changed");
            response.put("message", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
