package com.finalproject.finsera.finsera.dto.ewallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllEwalletResponse {
    Long ewalletId;
    String ewalletName;
    String ewalletImage;
}
