package com.finalproject.finsera.finsera.dto.customer;


import com.finalproject.finsera.finsera.model.enums.Gender;
import lombok.Data;

@Data
public class DetailCustomerResponse {

    private long idCustomer;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Gender gender;

}
