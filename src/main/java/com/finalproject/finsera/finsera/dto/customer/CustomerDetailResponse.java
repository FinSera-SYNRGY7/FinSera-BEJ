package com.finalproject.finsera.finsera.dto.customer;
import com.finalproject.finsera.finsera.model.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDetailResponse {
    private Long idCustomers;
    private String name;
    private String nik;
    private String address;
    private Gender gender;
    private String fatherName;
    private String motherName;
    private String phoneNumber;
    private Double income;
    private String username;
    private String password;
    private String email;
    private String mpin;
}


