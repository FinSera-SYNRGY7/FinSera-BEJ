package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.customer.CustomerDetailResponse;
import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;

public interface CustomerService {
    Customers register(RegisterRequestDto registerRequestDto);
    String getUserPin(String username);

}
