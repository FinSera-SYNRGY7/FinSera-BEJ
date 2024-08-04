package com.finalproject.finsera.finsera.dto.schemes;

import com.finalproject.finsera.finsera.dto.customer.DetailCustomerResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileExampleSwagger {

    private Integer code;
    private String message;
    private Boolean status;
    private DetailCustomerResponse data;
}
