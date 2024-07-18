package com.finalproject.finsera.finsera.dto.infosaldo;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class InfoSaldoRequest {

    @NotNull(message = "customerId cannot be null")
    private Long customerId;

}
