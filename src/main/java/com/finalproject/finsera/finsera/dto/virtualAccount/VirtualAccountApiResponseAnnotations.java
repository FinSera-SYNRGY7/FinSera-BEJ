package com.finalproject.finsera.finsera.dto.virtualAccount;

import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountExample;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

public class VirtualAccountApiResponseAnnotations {

    @Target({ ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer Virtual Account",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = TransferVirtualAccountExample.class)
                )}
            ),
            @ApiResponse(responseCode = "400", description = "Invalid Pin", content = @Content)
    })
    public @interface VirtualApiResponses{
    }
}
