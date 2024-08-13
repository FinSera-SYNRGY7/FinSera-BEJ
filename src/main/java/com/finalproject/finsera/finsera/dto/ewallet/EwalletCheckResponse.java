package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletCheckResponse {
    private String ewalletName;
    private long ewalletAccountId;
    private String ewalletAccount;
    private String ewalletAccountName;
}
