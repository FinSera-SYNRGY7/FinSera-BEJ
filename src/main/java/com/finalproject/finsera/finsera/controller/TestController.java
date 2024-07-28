package com.finalproject.finsera.finsera.controller;

import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> sayHello(

    ) {
        return ResponseEntity.ok(BaseResponse.success(null, "Hello World!"));
    }
}
