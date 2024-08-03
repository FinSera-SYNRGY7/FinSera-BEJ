package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.accountDummy.CreateAccountDummyRequestDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.service.AccountDummyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Create VA Controller", description = "API untuk operasi create VA")
public class AccountDummyController {
    @Autowired
    AccountDummyService accountDummyService;

    @PostMapping("create-virtual-account")
    @Operation(summary = "Create Virtual Accounts (done)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = AccountDummyData.class), mediaType = "application/json") })
    public ResponseEntity<AccountDummyData> create(@RequestBody CreateAccountDummyRequestDto createAccountDummyRequestDto){
        AccountDummyData accountDummyData = accountDummyService.createVirtualAccount(createAccountDummyRequestDto);
        return ResponseEntity.ok(accountDummyData);
    }
}
