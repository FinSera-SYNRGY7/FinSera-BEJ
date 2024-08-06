package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletCheckResponse {
    private long ewalletId;
    private String ewalletName;
    private String ewalletAccount;
    private String ewalletAccountName;
}
