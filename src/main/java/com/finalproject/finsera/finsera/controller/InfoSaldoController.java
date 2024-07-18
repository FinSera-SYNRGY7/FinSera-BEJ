package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amount")
public class InfoSaldoController {

    private final InfoSaldoService infoSaldoService;

    @GetMapping("/")
    public ResponseEntity<?> getInfoSaldo(
            @RequestBody InfoSaldoRequest infoSaldoRequest
    ) {

        return ResponseEntity.ok(BaseResponse.success(infoSaldoService.getInfoSaldo(infoSaldoRequest), "Nomor Rekening ditemukan"));
    }


}
