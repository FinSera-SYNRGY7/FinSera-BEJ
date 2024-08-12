package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletHistoryResponse {
    private long ewalletAccountId;
    private String ewalletName;
    private String ewalletImage;
    private String ewalletAccount;
    private String ewalletAccountName;

}
