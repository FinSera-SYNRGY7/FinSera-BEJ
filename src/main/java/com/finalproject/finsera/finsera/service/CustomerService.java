package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.register.RegisterRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;

public interface CustomerService {
    CustomerResponse findById(Long id);
    Customers register(RegisterRequestDto registerRequestDto);
}
