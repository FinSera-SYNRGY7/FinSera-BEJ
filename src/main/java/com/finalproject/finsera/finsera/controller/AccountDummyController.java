package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.accountDummy.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.service.AccountDummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountDummyController {
    @Autowired
    AccountDummyService accountDummyService;

    @PostMapping("create-virtual-account")
    public ResponseEntity<AccountDummyData> create(@RequestBody CreateAccountDummyRequestDto createAccountDummyRequestDto){
        AccountDummyData accountDummyData = accountDummyService.createVirtualAccount(createAccountDummyRequestDto);
        return ResponseEntity.ok(accountDummyData);
    }

    @GetMapping("all-transaction")
    public ResponseEntity<List<Transactions>> get(){
        return ResponseEntity.ok(accountDummyService.getAll());
    }

    @GetMapping("account-last-transaction")
    public ResponseEntity<List<AccountLastTransactionResponseDto>> getAccount(){
        return ResponseEntity.ok(accountDummyService.getAccount());
    }
}
