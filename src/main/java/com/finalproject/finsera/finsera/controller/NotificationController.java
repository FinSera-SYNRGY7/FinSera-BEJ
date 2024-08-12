package com.finalproject.finsera.finsera.controller;


import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.CustomerService;
import com.finalproject.finsera.finsera.service.NotificationService;
import com.finalproject.finsera.finsera.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notif")
public class NotificationController {
    @Autowired
    CustomerService customerService;


    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    NotificationService notificationService;


    @GetMapping({"", "/"})
    public ResponseEntity<?> getNotif(@RequestHeader(name = "Authorization") String token) {

        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);

        return ResponseEntity.ok(BaseResponse.success(notificationService.getNotif(userId), "Get Notif Success"));
    }

}
