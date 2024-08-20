package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletCheckAccountRequest;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletRequest;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.EwalletService;
import com.finalproject.finsera.finsera.service.impl.EwalletServiceImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Component
@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "Ewallet Controller", description = "API untuk operasi transaksi")
public class EwalletController {


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EwalletService ewalletService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/ewallet/check")
    @ApiResponseAnnotations.EwalletCheckApiResponses
    @Operation(summary = "Check Account E-wallet (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> checkTransaction(@RequestBody EwalletCheckAccountRequest ewalletCheckAccountRequest) {
        return ResponseEntity.ok(BaseResponse.success(ewalletService.checkAccountEwallet(ewalletCheckAccountRequest), "Akun e-wallet ditemukan"));

    }


    @PostMapping("/ewallet/create")
    @ApiResponseAnnotations.EwalletCreateApiResponses
    @Operation(summary = "Create Top Up Ewallet (done)" , security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createTransaction(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader("Authorization") String token,
            @RequestBody EwalletRequest ewalletRequest) {

        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);

        return ResponseEntity.ok(BaseResponse.success(ewalletService.createEwalletTransactions(userId, ewalletRequest), "Top Up berhasil"));

    }

    @GetMapping("/ewallet/history")
    @ApiResponseAnnotations.EwalletHistoryApiResponses
    @Operation(summary = "Ewallet history (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> historyTransaction(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        return ResponseEntity.ok(BaseResponse.success(ewalletService.historyTransactionEwallet(userId), "Ewallet history ditemukan"));

    }
    @GetMapping({"/ewallet/", "/ewallet"})
    @ApiResponseAnnotations.EwalletApiResponses
    @Operation(summary = "Get All E-Wallet (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllEwallet() {
        return ResponseEntity.ok(BaseResponse.success(ewalletService.getAllEwallet(), "Ewallet ditemukan"));
    }
}
