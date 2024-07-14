package com.finalproject.finsera.finsera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoSaldoResponse {

    @JsonProperty("signature")
    private String signature;
}
