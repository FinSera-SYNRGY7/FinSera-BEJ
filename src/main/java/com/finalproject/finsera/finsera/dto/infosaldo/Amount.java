package com.finalproject.finsera.finsera.dto.infosaldo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Amount {

    @Schema(name = "amount", example = "1000000")
    private Double amount;
    @Schema(name = "currency", example = "IDR")
    private String currency;

}
