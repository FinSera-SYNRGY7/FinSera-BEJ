package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import com.finalproject.finsera.finsera.util.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

//    @Override
//    public CustomerDetailResponse getUserDetailById(Long id) {
//        Optional<Customers> optionalCustomers = customerRepository.getUserDetailById(id);
//        CustomerDetailResponse customerResponse = modelMapper.map(optionalCustomers, CustomerDetailResponse.class);
//        return customerResponse;
//    }


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
        customers.setMpin(registerRequestDto.getMpin());
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
            if (optionalCustomers.isEmpty()){
                throw new UsernameNotFoundException("User not found");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            customers.setStatusUser(StatusUser.ACTIVE);
            customerRepository.save(customers);
            LoginResponseDto loginResponseDto = new LoginResponseDto(
                    jwt, userDetails.getUsername(), customers.getStatusUser()
            );
            return loginResponseDto;
    }

    @Override
    public ReloginResponseDto relogin(Principal principal) {
        String username = principal.getName();
        String userPin = getUserPin(username);
        Customers customers = customerRepository.findByUsername(username).get();
        ReloginResponseDto reloginResponseDto = new ReloginResponseDto(
                customers.getUsername(), customers.getStatusUser()
        );
        return reloginResponseDto;
    }
}
