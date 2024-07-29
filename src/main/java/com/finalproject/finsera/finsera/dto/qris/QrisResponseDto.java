package com.finalproject.finsera.finsera.dto.qris;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data

public class QrisResponseDto {

    private String username;
    private String accountNumber;
}
