package com.finalproject.finsera.finsera.util;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message){
        super(message);
    }
    
}
