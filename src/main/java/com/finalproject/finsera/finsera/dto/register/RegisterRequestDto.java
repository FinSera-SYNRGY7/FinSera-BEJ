package com.finalproject.finsera.finsera.dto.register;
import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequestDto {
    private String name;
    private String nik;
    private String address;
    private String gender;
    private String fatherName;
    private String motherName;
    private String phoneNumber;
    private Double income;
    private String username;
    private String password;
    private String email;
    private String mpin;
    private StatusUser statusUser;
}
