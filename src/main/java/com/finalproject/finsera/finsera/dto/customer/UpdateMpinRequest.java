package com.finalproject.finsera.finsera.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdateMpinRequest {
    @Schema(name = "mpinAuth", example = "567890")
    private String mpinAuth ;
}
