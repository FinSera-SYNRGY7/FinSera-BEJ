package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiRequestDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.dto.schemes.InfoSaldoExampleSwagger;
import com.finalproject.finsera.finsera.service.MutasiService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mutasi")
@Tag(name = "Mutasi Controller", description = "API untuk operasi mutasi")
public class MutasiController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MutasiService mutasiService;

    @GetMapping(value = {"/", ""})
    @Operation(summary = "Info Saldo user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = InfoSaldoExampleSwagger.class), mediaType = "application/json") })
    public ResponseEntity<?> getInfoMutasi(
            @Valid
            @RequestHeader(name = "Authorization") String token,
            @RequestBody MutasiRequestDto accountNumber,
            @RequestParam(value = "hariIni", required = false) boolean isToday,
            @RequestParam(value = "7Hari", required = false) boolean isSevenDays,
            @RequestParam(value = "1Bulan", required = false) boolean isOneMonth,
            @RequestParam(value = "tanggalMulai", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "tanggalAkhir", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);

        List<MutasiResponseDto> listTransction;
        if ((startDate != null) && (endDate != null)) {
            LocalDateTime startDateLocalDateTime = startDate.atStartOfDay();
            Timestamp startDateTimeStamp = Timestamp.valueOf(startDateLocalDateTime);
            LocalDateTime endDateLocalDateTime = endDate.atTime(23, 59, 59);
            Timestamp endDateTimeStamp = Timestamp.valueOf(endDateLocalDateTime);


            listTransction = mutasiService.getMutasi(
                    username, accountNumber, isSevenDays, isOneMonth, isToday, startDateTimeStamp, endDateTimeStamp, page, size
            );
        } else {
            listTransction = mutasiService.getMutasi(
                    username, accountNumber, isSevenDays, isOneMonth, isToday, null, null, page, size
            );

        }
        return ResponseEntity.ok(BaseResponse.success(listTransction, "Nomor Rekening ditemukan"));
    }


}
