package com.finalproject.finsera.finsera.controller;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.finsera.finsera.dto.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionResponseDto;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.service.impl.TransactionServiceImpl;

@Component
@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired TransactionServiceImpl transactionServiceImpl;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody TransactionRequestDto transactionRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionResponseDto transactionResponseDto =  transactionServiceImpl.create(transactionRequestDto);
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

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkTransaction(@RequestBody TransactionCheckAccountRequestDto transactionCheckAccountRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionCheckAccountResponseDto transactionCheckAccountResponseDto =  transactionServiceImpl.check(transactionCheckAccountRequestDto);
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
}
