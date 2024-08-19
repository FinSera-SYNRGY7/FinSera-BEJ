package com.finalproject.finsera.finsera.dto.qris;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrisMerchantResponseDto {

    private String transaction_num;
    private String transaction_date;
    private String name_sender;
    private String accountnum_sender;
    private String name_recipient;
    private String accountnum_recipient;
    private String nominal;

}
