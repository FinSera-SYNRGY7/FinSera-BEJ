package com.finalproject.finsera.finsera.dto.customer;

import com.finalproject.finsera.finsera.model.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponse {
    private Long idCustomer;
    private String username;
}
