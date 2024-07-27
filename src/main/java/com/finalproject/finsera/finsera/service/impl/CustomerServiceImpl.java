package com.finalproject.finsera.finsera.service.impl;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        customers.setMpin(passwordEncoder.encode(registerRequestDto.getMpin()));
        customers.setStatusUser(StatusUser.INACTIVE);

        customerRepository.save(customers);
        return customers;
    }

    @Override
    public String getUserPin(String username) {
        Customers customers = customerRepository.findByUsername(username).get();
        return customers.getMpin();
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
                    token, refreshToken, userDetails.getIdCustomers(), customers.getMpin(), customers.getStatusUser()
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

//    @Override
//    public String relogin(String username, ReloginRequestDto reloginRequestDto) {
//        Customers customers = customerRepository.findByUsername(username).get();
//        System.out.println(customers.getMpin());
//        System.out.println(reloginRequestDto.getMpin());
//        if (passwordEncoder.matches(customers.getMpin(), passwordEncoder.encode(reloginRequestDto.getMpin()))){
//            return "Pin Valid";
//        } else {
//            return "Pin Invalid";
//        }
//    }

    @Override
    public String relogin(Long id, ReloginRequestDto reloginRequestDto) {
        Customers customers = customerRepository.findById(id).get();
        if (passwordEncoder.matches(reloginRequestDto.getMpin(), customers.getMpin())){
            return "Pin Valid";
        } else {
            return "Pin Invalid";
        }
    }
}
