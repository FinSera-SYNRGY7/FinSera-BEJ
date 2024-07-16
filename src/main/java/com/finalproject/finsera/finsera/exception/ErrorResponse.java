package com.finalproject.finsera.finsera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorResponse {
    public ResponseEntity<Map<String, Object>> message(String message){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
