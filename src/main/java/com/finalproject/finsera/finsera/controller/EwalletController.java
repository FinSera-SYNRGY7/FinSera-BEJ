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
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Check Account E-wallet (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<?> checkTransaction(@RequestBody EwalletCheckAccountRequest ewalletCheckAccountRequest) {
        return ResponseEntity.ok(BaseResponse.success(ewalletService.checkAccountEwallet(ewalletCheckAccountRequest), "Akun e-wallet ditemukan"));

    }


//    @PostMapping("/ewallet/create")
//    @Operation(summary = "Top Up Ewallet (done)" , security = @SecurityRequirement(name = "bearerAuth"))
//    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionResponseDto.class), mediaType = "application/json") })
//    public ResponseEntity<?> createTransaction(@RequestHeader("Authorization") String token, @RequestBody EwalletRequest ewalletRequest) {
//
//        String jwt = token.substring("Bearer ".length());
//        Long userId = jwtUtil.getId(jwt);
//
//        return ResponseEntity.ok(BaseResponse.success(infoSaldoService.getInfoSaldo(username), "Nomor Rekening ditemukan"));
//
//    }
//
//
//    @GetMapping("/ewallet/history")
//    @Operation(summary = "Transaction history intra-bank", security = @SecurityRequirement(name = "bearerAuth"))
//    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
//    public ResponseEntity<Map<String, Object>> historyTransaction(@RequestHeader("Authorization") String token) {
//
//    }
//
//    @GetMapping({"/ewallet/", "/ewallet"})
//    @Operation(summary = "Transaction history intra-bank", security = @SecurityRequirement(name = "bearerAuth"))
//    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
//    public ResponseEntity<Map<String, Object>> historyTransaction() {
//
//    }

    @GetMapping({"/ewallet/", "/ewallet"})
    @Operation(summary = "Get All E-Wallet", security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<?> getAllEwallet() {
        return ResponseEntity.ok(BaseResponse.success(ewalletService.getAllEwallet(), "Ewallet ditemukan"));
    }
}
