package com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferVirtualAccountRequestDto {
    @Schema(name = "virtualAccountNumber", example = "9876543")
    String virtualAccountNumber;
    @Schema(name = "mpinAccount", example = "123456")
    String mpinAccount;
}