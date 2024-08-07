package com.finalproject.finsera.finsera.exception;

import com.finalproject.finsera.finsera.dto.BaseResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<BaseResponse<String>> handleExpiredJwtException(ExpiredJwtException ex) {
        String errorMessage = String.format("JWT expired at %s. Current time: %s, a difference of %d milliseconds. Allowed clock skew: %d milliseconds.",
                ex.getClaims().getExpiration(), ex.getClaims().getIssuedAt(), ex.getClaims().getExpiration().getTime() - ex.getClaims().getIssuedAt().getTime(), 0);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.failure(HttpStatus.UNAUTHORIZED.value(), errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.failure(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<BaseResponse<String>> handleGeneralException(Exception exception) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(BaseResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.toString()));
//    }


    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(BaseResponse.failure(exception.getStatusCode().value(), exception.getReason()));
    }




}