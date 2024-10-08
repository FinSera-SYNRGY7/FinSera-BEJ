package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.service.MutasiService;
import com.finalproject.finsera.finsera.util.ApiResponseAnnotations;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    @ApiResponseAnnotations.MutasiApiResponses
    @Operation(summary = "Mutasi (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getInfoMutasi(
            @Valid
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "startDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
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
                    username, startDateTimeStamp, endDateTimeStamp, page, size
            );
        } else if((startDate != null) && (endDate == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End Date is required, if you want to use Start Date");
        } else if ((startDate == null) && (endDate != null)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start Date is required, if you want to use End Date");
        } else {
            listTransction = mutasiService.getMutasi(
                    username,  null, null, page, size
            );
        }
        return ResponseEntity.ok(BaseResponse.success(listTransction, "Nomor Rekening ditemukan"));
    }


    @GetMapping(value = {"/download/", "/download"})
    @ApiResponseAnnotations.MutasiDownloadResponses
    @Operation(summary = "Download Mutasi (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getDownloadInfoMutasi(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @Valid
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "startDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Timestamp startDateTimeStamp = null;
        Timestamp endDateTimeStamp = null;
        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        byte[] reportContent;
        if ((startDate != null) && (endDate != null)) {
            LocalDateTime startDateLocalDateTime = startDate.atStartOfDay();
            startDateTimeStamp = Timestamp.valueOf(startDateLocalDateTime);
            LocalDateTime endDateLocalDateTime = endDate.atTime(23, 59, 59);
            endDateTimeStamp = Timestamp.valueOf(endDateLocalDateTime);
            reportContent = mutasiService.transactionsReport(username, startDateTimeStamp, endDateTimeStamp);
        } else if((startDate != null) && (endDate == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End Date is required, if you want to use Start Date");
        } else if ((startDate == null) && (endDate != null)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start Date is required, if you want to use End Date");
        } else {
            reportContent = mutasiService.transactionsReport(username, null, null);
        }
        ByteArrayResource resource = new ByteArrayResource(reportContent);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("Transactions-Report (" + startDateTimeStamp + " - " + endDateTimeStamp + ").pdf")
                                .build().toString())
                .body(resource);
    }

}
