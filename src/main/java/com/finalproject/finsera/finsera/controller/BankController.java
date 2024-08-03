package com.finalproject.finsera.finsera.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.finsera.finsera.service.impl.BankServiceImpl;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Component
@RestController
@RequestMapping("/api/v1/bank")
@Tag(name = "Bank Controller", description = "API untuk Data Bank")
public class BankController {
    @Autowired BankServiceImpl bankServiceImpl;

    @GetMapping("/get-all")
    @Operation(summary = "List All Bank Data", security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransactionCheckAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> historyTransactionInterBank(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<?> dataList =  bankServiceImpl.getListBanks();
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Data Bank tersedia");
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
