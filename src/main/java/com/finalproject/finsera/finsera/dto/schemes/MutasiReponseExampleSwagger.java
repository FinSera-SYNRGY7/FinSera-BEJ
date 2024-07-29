package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MutasiReponseExampleSwagger {
    private Integer code;
    private String message;
    private Boolean status;
    private MutasiResponseDto data;
}
