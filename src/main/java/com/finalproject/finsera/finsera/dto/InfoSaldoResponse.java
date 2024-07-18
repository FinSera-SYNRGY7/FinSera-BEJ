package com.finalproject.finsera.finsera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoSaldoResponse {

    private long customerId;
    private String username;
    private String accountNumber;
    private Amount amount;

}
