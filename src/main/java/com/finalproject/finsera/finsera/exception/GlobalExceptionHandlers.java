package com.finalproject.finsera.finsera.exception;

import com.finalproject.finsera.finsera.dto.BaseResponse;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<BaseResponse<String>> handleExpiredJwtException(ExpiredJwtException ex) {
        String errorMessage = String.format("JWT expired at %s. Current time: %s, a difference of %d milliseconds. Allowed clock skew: %d milliseconds.",
                ex.getClaims().getExpiration(), ex.getClaims().getIssuedAt(), ex.getClaims().getExpiration().getTime() - ex.getClaims().getIssuedAt().getTime(), 0);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.failure(HttpStatus.UNAUTHORIZED.value(), errorMessage));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.failure(404, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGeneralException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<String>> handleCustomException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(BaseResponse.failure(exception.getStatusCode().value(), exception.getReason()));
    }



}