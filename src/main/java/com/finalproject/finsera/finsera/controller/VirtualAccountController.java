package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/va")
@Tag(name = "Virtual Account Controller", description = "API untuk operasi virtual account")
public class VirtualAccountController {
    @Autowired
    VirtualAccountService virtualAccountService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/create-virtual-account")
    @Operation(summary = "Create Virtual Accounts (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> create(@RequestBody CreateVirtualAccountRequestDto createVirtualAccountRequestDto){
        VirtualAccounts virtualAccounts = virtualAccountService.createVirtualAccount(createVirtualAccountRequestDto);
        return ResponseEntity.ok(BaseResponse.success(virtualAccounts, "Transfer Virtual Accoount berhasil"));
    }

    @GetMapping("/va-last-transaction")
    @Operation(summary = "Last Transactions Virtual Accounts (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAccount(){
        return ResponseEntity.ok(BaseResponse.success(virtualAccountService.getLastTransactionAccountVA(), "Berhasil mendapatkan data"));
    }

    @PostMapping("/check-virtual-account")
    @Operation(summary = "Check Virtual Accounts (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> checkVirtualAccount(@RequestBody CheckVirtualAccountRequestDto checkVirtualAccountRequestDto){
        return ResponseEntity.ok(BaseResponse.success(virtualAccountService.checkVirtualAccount(checkVirtualAccountRequestDto), "Nomor Virtual Account ditemukan"));
    }

    @PostMapping("/transfer-va")
    @Operation(summary = "Transfer Virtual Accounts (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TransferVirtualAccountResponseDto.class), mediaType = "application/json") })
    public ResponseEntity<?> virtualAccount(@RequestHeader("Authorization") String token, @RequestBody TransferVirtualAccountRequestDto transferVirtualAccountRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);

        return ResponseEntity.ok(BaseResponse.success(virtualAccountService.transferVA(userId, transferVirtualAccountRequestDto), "Transfer Virtual Account Berhaisl"));
    }
}
