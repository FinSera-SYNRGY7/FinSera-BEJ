package com.finalproject.finsera.finsera.dto.infosaldo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoSaldoResponse {

    @Schema(name = "customerId", example = "1")
    private long customerId;
    @Schema(name = "name", example = "John Doe")
    private String name;
    @Schema(name = "accountNumber", example = "123456789")
    private String accountNumber;
    private Amount amount;

}
