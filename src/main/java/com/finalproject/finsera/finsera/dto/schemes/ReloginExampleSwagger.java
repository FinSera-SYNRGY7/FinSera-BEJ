package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.login.ReloginResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReloginExampleSwagger {
    private String message;
    private ReloginResponseDto data;
}
