package com.finalproject.finsera.finsera.dto.qris;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data

public class QrisResponseDto {

    @Schema(name = "username", example = "johndoe")
    private String username;
    @Schema(name = "accountNumber", example = "123456789")
    private String accountNumber;
}
