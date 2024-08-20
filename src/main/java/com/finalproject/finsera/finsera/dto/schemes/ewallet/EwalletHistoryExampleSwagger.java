package com.finalproject.finsera.finsera.dto.schemes.ewallet;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletHistoryResponse;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EwalletHistoryExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Ewallet history ditemukan")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    @ArraySchema(schema = @Schema(anyOf = { EwalletHistoryResponse.class, EwalletResponse.class }))
    private List<EwalletHistoryResponse> data;



}
