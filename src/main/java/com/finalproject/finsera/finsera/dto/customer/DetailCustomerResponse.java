package com.finalproject.finsera.finsera.dto.customer;


import lombok.Data;

@Data
public class DetailCustomerResponse {

    private long idCustomer;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;

}
