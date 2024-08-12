package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.virtualAccount.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/va")
public class VirtualAccountController {
    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    VirtualAccountService virtualAccountService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("create-virtual-account")
    public ResponseEntity<VirtualAccounts> create(@RequestBody CreateVirtualAccountRequestDto createVirtualAccountRequestDto){
        VirtualAccounts virtualAccounts = virtualAccountService.createVirtualAccount(createVirtualAccountRequestDto);
        return ResponseEntity.ok(virtualAccounts);
    }

    @GetMapping("all-transaction")
    public ResponseEntity<List<Transactions>> get(){
        return ResponseEntity.ok(virtualAccountService.getAll());
    }

    @GetMapping("account-last-transaction")
    public ResponseEntity<Map<String, Object>> getAccount(){
        return virtualAccountService.getAccount();
    }

    @PostMapping("check-virtual-account")
    public ResponseEntity<Map<String, Object>> checkVirtualAccount(@RequestBody CheckVirtualAccountRequestDto checkVirtualAccountRequestDto){
        return virtualAccountService.checkVirtualAccount(checkVirtualAccountRequestDto);
    }

    @PostMapping("/virtual-account")
    @Operation(summary = "Virtual Accounts", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransferVirtualAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<Map<String, Object>> virtualAccount(@RequestHeader("Authorization") String token, @RequestBody TransferVirtualAccountRequestDto transferVirtualAccountRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(userId);


//        TransferVirtualAccountResponseDto transferResponse = virtualAccountService
//                .transferVA(
//                        userId,
//                        virtualAccounts.get().getVirtualAccountNumber(),
//                        bankAccounts.getAccountNumber(),
//                        transferVirtualAccountRequestDto
//                        );
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "success");
//        response.put("data", transferResponse);
//        return response;
        return null;


//        if (!accountDummyData.isPresent()){
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "recipient account not found");
//            response.put("data", null);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } else if (bankAccounts.getAmount() < transferVirtualAccountRequestDto.getNominal()) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "your balance is insufficient");
//            response.put("data", null);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } else if (!bankAccounts.getMpinAccount().equals(transferVirtualAccountRequestDto.getMpinAccount())) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "your pin is invalid");
//            response.put("data", null);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } else {
//            TransferVirtualAccountResponseDto transferVirtualAccount = virtualAccountService.transferVA(userId, transferVirtualAccountRequestDto);
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "success");
//            response.put("data", transferVirtualAccount);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
    }
}
