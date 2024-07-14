package com.finalproject.finsera.finsera.controller;


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
public class InfoSaldoController {

    private final InfoSaldoService infoSaldoService;

    @GetMapping("/")
    public ResponseEntity<?> getInfoSaldo() {

        return ResponseEntity.ok(BaseResponse.success(infoSaldoService, "Success Create Order"));
    }


}
