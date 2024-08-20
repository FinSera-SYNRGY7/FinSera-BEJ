package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.dto.schemes.qris.QrisMerchantExampleSwagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.awt.print.Book;
import java.lang.annotation.*;

public class ApiResponseAnnotations {
 
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pembayaran Qris",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = QrisMerchantExampleSwagger.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
            content = @Content), 
        @ApiResponse(responseCode = "404", description = "User not found", 
            content = @Content) })
    public @interface QrisMerchantApiResponses {
    }

//    @Target({ ElementType.METHOD, ElementType.TYPE })
//    @Retention(RetentionPolicy.RUNTIME)
//    @Documented
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "201", description = "Created the vendor",
//            content = { @Content(mediaType = "application/json",
//            schema = @Schema(implementation = Author.class)) }),
//        @ApiResponse(responseCode = "400", description = "Invalid data supplied",
//            content = @Content),
//        @ApiResponse(responseCode = "409", description = "Vendor already exists",
//            content = @Content) })
//    public @interface VendorApiResponses {
//    }
//
    //Marcel silakan disesuaikan isi dari annotationnya.
}
 