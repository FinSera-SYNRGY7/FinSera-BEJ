package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwalletResponse {
    private String transactionNum;
    private String transactionDate;
    private String nameSender;
    private String accountSender;
    private String ewalletName;
    private String ewalletAccountName;
    private String ewalletAccount;
    private String nominal;
    private String feeAdmin;
    private String note;
}
