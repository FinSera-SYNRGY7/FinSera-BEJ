package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.virtualAccount.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/va")
public class VirtualAccountController {
    @Autowired
    VirtualAccountService virtualAccountService;

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
}
