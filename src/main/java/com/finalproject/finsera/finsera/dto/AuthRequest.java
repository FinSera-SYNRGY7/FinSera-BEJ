package com.finalproject.finsera.finsera.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthRequest {

    @JsonProperty("grantType")
    private String grantType;
}
