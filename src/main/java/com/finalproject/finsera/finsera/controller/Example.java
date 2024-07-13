package com.finalproject.finsera.finsera.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class Example {
    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
