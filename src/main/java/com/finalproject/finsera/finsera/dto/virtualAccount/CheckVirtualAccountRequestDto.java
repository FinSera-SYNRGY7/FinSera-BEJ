package com.finalproject.finsera.finsera.dto.virtualAccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheckVirtualAccountRequestDto {
    @Schema(name = "virtualAccountNumber", example = "9876543")
    private String virtualAccountNumber;
}
