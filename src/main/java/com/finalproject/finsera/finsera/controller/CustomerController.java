package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ProfileExampleSwagger;
import com.finalproject.finsera.finsera.exception.ErrorResponse;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profiling Controller", description = "API untuk operasi detail user")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping(value = {"/", ""})
    @Operation(summary = "Detail user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ProfileExampleSwagger.class), mediaType = "application/json") })
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
