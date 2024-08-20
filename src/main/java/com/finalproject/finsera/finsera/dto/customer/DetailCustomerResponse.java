package com.finalproject.finsera.finsera.dto.customer;


import com.finalproject.finsera.finsera.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DetailCustomerResponse {

    @Schema(name = "idCustomer", example = "1")
    private long idCustomer;
    @Schema(name = "username", example = "johndoe")
    private String username;
    @Schema(name = "name", example = "John Doe")
    private String name;
    @Schema(name = "email", example = "john.doe@example.com")
    private String email;
    @Schema(name = "phone", example = "081234567890")
    private String phone;
    @Schema(name = "address", example = "123 Main Street")
    private String address;
    @Schema(name = "gender", example = "LAKI_LAKI")
    private Gender gender;

}
