package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.service.AccountDummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountDummyController {
    @Autowired
    AccountDummyService accountDummyService;

    @PostMapping("create-virtual-account")
    public ResponseEntity<AccountDummyData> create(@RequestBody CreateAccountDummyRequestDto createAccountDummyRequestDto){
        AccountDummyData accountDummyData = accountDummyService.createVirtualAccount(createAccountDummyRequestDto);
        return ResponseEntity.ok(accountDummyData);
    }
}
