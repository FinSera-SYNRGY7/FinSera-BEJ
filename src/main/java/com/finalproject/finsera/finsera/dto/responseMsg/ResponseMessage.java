package com.finalproject.finsera.finsera.dto.responseMsg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage<T> {
    private int statusCode;
    private T data;
    private String message;

    public ResponseMessage(int statusCode, T data, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
    }
}