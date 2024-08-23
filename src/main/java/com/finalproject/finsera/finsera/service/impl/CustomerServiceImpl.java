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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        }
        Customers customer = optionalCustomers.get();
        if(!newMpin.matches("\\d{6}")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin App Lock harus 6 digit angka");
        }
        if (passwordEncoder.matches(newMpin, customer.getMpinAuth())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pin App Lock sudah digunakan sebelumnya");
        }
        customer.setMpinAuth(passwordEncoder.encode(newMpin));

        customerRepository.save(customer);
        return customer;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<Customers> customer = Optional.ofNullable(customerRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")
        ));
        Customers customers = customer.get();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), customers.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username atau password yang anda masukkan salah!");
        } else if (customer.get().getStatusUser() == StatusUser.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun anda tidak aktif!");
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername().toString(),
                            loginRequestDto.getPassword().toString()
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
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        String username = jwtUtilRefreshToken.getUsername(refreshTokenRequestDto.getRefreshToken());
        String token = jwtUtil.generateTokenFromUsername(username);
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")
        ));

        if (customers.get().getStatusUser() == StatusUser.INACTIVE){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun anda tidak aktif");
        } else {
            RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
            refreshTokenResponseDto.setAccessToken(token);
            return refreshTokenResponseDto;
        }

    }

    @Override
    public ResponseEntity<Map<String, Object>> relogin(Long id, ReloginRequestDto reloginRequestDto) {
        Customers customers = customerRepository.findById(id).get();
        if (customers.getStatusUser().equals(StatusUser.INACTIVE)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,  "Akun anda tidak aktif");
        }
        if (!passwordEncoder.matches(reloginRequestDto.getMpinAuth(), customers.getMpinAuth())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pin yang anda masukkan salah");
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> forgotMpin(Long id, ForgotMpinRequestDto forgotMpinRequestDto) {
        Customers customers = customerRepository.findById(id).get();

        if (!passwordEncoder.matches(forgotMpinRequestDto.getPassword(), customers.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password yang anda masukkan salah");
        } else if (!forgotMpinRequestDto.getNewMpin().equalsIgnoreCase(forgotMpinRequestDto.getConfirmNewMpin())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "MPin yang anda masukkan salah");
        } else {
            customers.setPassword(passwordEncoder.encode(forgotMpinRequestDto.getPassword()));
            customers.setMpinAuth(passwordEncoder.encode(forgotMpinRequestDto.getNewMpin()));
            customerRepository.save(customers);
            return null;
        }
    }

}
