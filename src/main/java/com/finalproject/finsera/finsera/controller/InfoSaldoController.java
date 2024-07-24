package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amount")
@Tag(name = "Informasi Saldo Controller", description = "API untuk operasi informasi saldo")
public class InfoSaldoController {

    private final InfoSaldoService infoSaldoService;

    private final JwtUtil jwtUtil;

    @GetMapping(value = {"/", ""})
    @Operation(summary = "Info Saldo user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getInfoSaldo(
            @Valid
            @RequestHeader(name = "Authorization") String token
    ) {

        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        return ResponseEntity.ok(BaseResponse.success(infoSaldoService.getInfoSaldo(username), "Nomor Rekening ditemukan"));
    }


}
