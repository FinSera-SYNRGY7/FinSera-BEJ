package com.finalproject.finsera.finsera.util;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ConnectionException {
     @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Map<String, Object>> handleConnectException(ConnectException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("message", "Connection lost with the server. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
}
