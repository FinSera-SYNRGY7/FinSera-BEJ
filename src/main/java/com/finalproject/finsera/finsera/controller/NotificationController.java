package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.service.NotificationService;
import com.finalproject.finsera.finsera.util.ApiResponseAnnotations;
import com.finalproject.finsera.finsera.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notif")
@Tag(name = "Notification Controller", description = "API untuk Get Notification")
public class NotificationController {
    @Autowired
    CustomerService customerService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    NotificationService notificationService;


    @GetMapping({"", "/"})
    @ApiResponseAnnotations.NotificationResponses
    @Operation(summary = "Notification (done)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getNotif(
            @Parameter(description = "Example header value", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJpYXQiOjE3MjQxMjA2NTZ9.1xUZqr42tkDH4x31q9gJd3TmMMRGouRhCixe9BmtI6Y")
            @RequestHeader(name = "Authorization") String token
    ) {

        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);

        return ResponseEntity.ok(BaseResponse.success(notificationService.getNotif(userId), "Data notifkasi ditemukan"));
    }

}
