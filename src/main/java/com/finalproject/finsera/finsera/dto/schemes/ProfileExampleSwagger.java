package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.customer.DetailCustomerResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileExampleSwagger {

    @Schema(name = "code", example = "200")
    private Integer code;
    @Schema(name = "message", example = "Berhasil mendapatkan detail user")
    private String message;
    @Schema(name = "status", example = "true")
    private Boolean status;
    private DetailCustomerResponse data;
}
