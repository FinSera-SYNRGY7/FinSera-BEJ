package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.exception.ErrorResponse;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> userDetail(@RequestHeader(name = "Authorization") String token) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        Map<String, Object> data = new HashMap<>();
        Optional<Customers> customer = customerRepository.findByUsername(username);

        if (customer != null) {
            data.put("userDetails", customer);
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
