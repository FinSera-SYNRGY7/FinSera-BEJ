package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.customer.DetailCustomerResponse;
import com.finalproject.finsera.finsera.dto.customer.UpdateMpinRequest;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import com.finalproject.finsera.finsera.dto.responseMsg.ResponseConstant;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ProfileExampleSwagger;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.util.ApiResponseAnnotations;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ApiResponseAnnotations.ProfileResponses
    @Operation(summary = "Detail user (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> userDetail(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader(name = "Authorization") String token) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        Optional<Customers> customer = customerRepository.findByUsername(username);
        DetailCustomerResponse detailCustomerResponse = new DetailCustomerResponse();
        detailCustomerResponse.setAddress(customer.get().getAddress());
        detailCustomerResponse.setIdCustomer(customer.get().getIdCustomers());
        detailCustomerResponse.setName(customer.get().getName());
        detailCustomerResponse.setEmail(customer.get().getEmail());
        detailCustomerResponse.setGender(customer.get().getGender());
        detailCustomerResponse.setPhone(customer.get().getPhoneNumber());
        detailCustomerResponse.setUsername(customer.get().getUsername());

        if (customer != null) {
            return ResponseEntity.ok(BaseResponse.success(detailCustomerResponse, "Berhasil mendapatkan detail user"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        }
    }

    @PatchMapping("/update-mpin")
    @ApiResponseAnnotations.UpdateMpinResponses
    @Operation(summary = "Update PIN AppLock (done) ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BaseResponse<Void>> updateMpin(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader(name = "Authorization") String token,
            @RequestBody UpdateMpinRequest mpin) {
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);

        Customers updatedCustomer =  customerService.updateMpin(username, mpin.getMpinAuth());
        return ResponseEntity.ok(BaseResponse.success(null, ResponseConstant.UPDATE_SUCCESS));

    }


}
