package com.finalproject.finsera.finsera.controller;

import java.net.ConnectException;
import java.util.*;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

import com.finalproject.finsera.finsera.dto.transaction.*;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.service.impl.TransactionServiceImpl;

@Component
@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transactions Controller", description = "API untuk operasi transaksi")
public class TransactionController {
    @Autowired TransactionServiceImpl transactionServiceImpl;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    VirtualAccountService virtualAccountService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/transaction-intra/create")
    @Operation(summary = "Transaction intra-bank (done)" , security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDto transactionRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.substring("Bearer ".length());
            Long userId = jwtUtil.getId(jwt);

            TransactionResponseDto transactionResponseDto =  transactionServiceImpl.placeTransactionsIntraBank(transactionRequestDto, userId);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Transaksi Berhasil");
            response.put("status", 200);
            response.put("data", transactionResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            if (e instanceof ConnectException){
                response.put("status", 503);
                response.put("message", "Connection lost with the server. Please try again later.");
                return ResponseEntity.status(503).body(response);
            }
            else{
                response.put("status", 402);
                response.put("message", e.getMessage());
                return ResponseEntity.status(402).body(response);
            }

        }
    }

    @PostMapping("/transaction-intra/check")
    @Operation(summary = "Transaction check intra-bank (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> checkTransaction(@RequestBody TransactionCheckAccountRequestDto transactionCheckAccountRequestDto) {
        System.out.println(transactionCheckAccountRequestDto.getAccountnum_recipient());
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionCheckAccountResponseDto transactionCheckAccountResponseDto =  transactionServiceImpl.checkAccountIntraBank(transactionCheckAccountRequestDto);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Data Rekening tersedia");
            response.put("status", 200);
            response.put("data", transactionCheckAccountResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

    @GetMapping("/transaction-intra/history")
    @Operation(summary = "Transaction history intra-bank (done)", security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> historyTransaction(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.substring("Bearer ".length());
            Long userId = jwtUtil.getId(jwt);
            List<?> dataList =  transactionServiceImpl.historyTransaction(userId);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "History Transaksi tersedia");
            response.put("status", 200);
            response.put("data", dataList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

    @PostMapping(value ={"/transaction-inter/create"})
    @Operation(summary = "Transaction inter-bank (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionOtherBankResponse.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> createTransactionInter(@RequestHeader("Authorization") String token, @RequestBody TransactionOtherBankRequest transactionOtherBankRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.substring("Bearer ".length());
            Long userId = jwtUtil.getId(jwt);
            TransactionOtherBankResponse transactionResponseDto =  transactionServiceImpl.placeTransactionsInterBank(transactionOtherBankRequest, userId);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Transaksi Berhasil");
            response.put("status", 200);
            response.put("data", transactionResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            if (e instanceof ConnectException){
                response.put("status", 503);
                response.put("message", "Connection lost with the server. Please try again later.");
                return ResponseEntity.status(503).body(response);
            }
            else{
                response.put("status", 402);
                response.put("message", e.getMessage());
                return ResponseEntity.status(402).body(response);
            }

        }
    }
    @PostMapping(value ={"/transaction-inter/check"})
    @Operation(summary = "Transaction check inter-bank (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckOtherBankResponse.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> checkTransactionInter(@RequestBody TransactionCheckOtherBankAccountRequest transactionCheckOtherBankAccountRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionCheckOtherBankResponse transactionCheckAccountResponseDto =  transactionServiceImpl.checkAccountOtherBank(transactionCheckOtherBankAccountRequest);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Data Rekening tersedia");
            response.put("status", 200);
            response.put("data", transactionCheckAccountResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

    @GetMapping("/transaction-inter/history")
    @Operation(summary = "Transaction history inter-bank (done)", security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> historyTransactionInterBank(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.substring("Bearer ".length());
            Long userId = jwtUtil.getId(jwt);
            List<?> dataList =  transactionServiceImpl.historyTransactionInterBank(userId);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "History Transaksi tersedia");
            response.put("status", 200);
            response.put("data", dataList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

}
