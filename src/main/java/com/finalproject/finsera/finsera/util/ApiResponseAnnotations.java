package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.dto.schemes.*;
import com.finalproject.finsera.finsera.dto.schemes.ewallet.EwalletCheckExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ewallet.EwalletCreateExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.ewallet.EwalletHistoryExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.qris.QrisMerchantExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.qris.QrisResponseExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.transactions.TransactionsCheckExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.transactions.TransactionsCreateExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.transactions.TransactionsInterCheckExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.transactions.TransactionsInterCreateExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.va.VaCheckExampleSwagger;
import com.finalproject.finsera.finsera.dto.schemes.va.VaTransferExampleSwagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            @ApiResponse(responseCode = "200", description = "Ok (Pembayaran Qris)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QrisMerchantExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "MPIN Account salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun terblokir",
                                            value = "{\"code\": 401, \"message\": \"Akun anda terblokir\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT kedaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "402", description = "Payment Required (Saldo tidak cukup)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 402, \"message\": \"Saldo Anda Tidak Cukup\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (User tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface QrisMerchantApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Get Data Qris)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QrisResponseExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface QrisApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Top Up E-Wallet Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EwalletCreateExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "MPIN Account salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun terblokir",
                                            value = "{\"code\": 401, \"message\": \"Akun anda terblokir\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT kedaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "402", description = "Payment Required (Saldo tidak cukup)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 402, \"message\": \"Saldo Anda Tidak Cukup\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "E-Wallet tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"E-Wallet tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor E-Wallet tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor E-Wallet tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface EwalletCreateApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Cek akun E-Wallet berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EwalletCheckExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "E-Wallet tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"E-Wallet tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor E-Wallet tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor E-Wallet tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface EwalletCheckApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Ewallet history ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 200, \"message\": \"Ewallet history ditemukan\", \"status\": true, \"data\": [{\"ewalletAccountId\": 1, \"ewalletName\": \"DANA\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/dana.png\", \"ewalletAccount\": \"089123123123\", \"ewalletAccountName\": \"Rahmat\"}, {\"ewalletAccountId\": 2, \"ewalletName\": \"OVO\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/ovo.png\", \"ewalletAccount\": \"0891234454467\", \"ewalletAccountName\": \"Ahmad\"}]}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface EwalletHistoryApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Ewallet ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 200, \"message\": \"Ewallet ditemukan\", \"status\": true, \"data\": [" +
                                            "{\"ewalletId\": 1, \"ewalletName\": \"DANA\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/dana.png\"}, " +
                                            "{\"ewalletId\": 3, \"ewalletName\": \"Gopay\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/gopay.png\"}, " +
                                            "{\"ewalletId\": 2, \"ewalletName\": \"OVO\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/ovo.png\"}, " +
                                            "{\"ewalletId\": 5, \"ewalletName\": \"PayPal\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/paypal.png\"}, " +
                                            "{\"ewalletId\": 4, \"ewalletName\": \"ShopeePay\", \"ewalletImage\": \"https://storage.googleapis.com/image_bank_ewallet/ewallet/shopeepay.png\"}]}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"E-Wallet tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface EwalletApiResponses {
    }


    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Nomor rekening ditemukan)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InfoSaldoExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor Rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor Rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface InformasiSaldoApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Transfer VA Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaTransferExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "MPIN Account salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun terblokir",
                                            value = "{\"code\": 401, \"message\": \"Akun anda terblokir\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT Kadaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "402", description = "Payment Required (Saldo tidak cukup)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 402, \"message\": \"Saldo Anda Tidak Cukup\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor Virtual Account tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor Virtual Account tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),

                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface VaTransferApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Cek nomor VA berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaCheckExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Nomor Virtual Account tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 404, \"message\": \"Nomor Virtual Account tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface VaCheckApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Ewallet ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 200, \"message\": \"Berhasil mendapatkan data\", \"status\": true, \"data\": [" +
                                            "{\"accountName\": \"BRIVA\", \"accountNumber\": \"123456789\"}, " +
                                            "{\"accountName\": \"BCA Virtual Account\", \"accountNumber\": \"9876543\"}]}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Transaksi tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface VaLastTransactionApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Berhasil mendapatkan access token baru)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Akun terblokir)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Akun tidak aktif",
                                            value = "{\"code\": 401, \"message\": \"Akun anda tidak aktif\", \"status\": false , \"data\": null}"
                                    ),
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (User tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),

            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface RefreshTokenApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Login Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Username atau password yang anda masukkan salah",
                                            value = "{\"code\": 401, \"message\": \"Username atau password yang anda masukkan salah!\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun tidak aktif",
                                            value = "{\"code\": 401, \"message\": \"Akun anda tidak aktif!\", \"status\": false , \"data\": null}"
                                    ),
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (User tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),

                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface LoginResponses {
    }
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Relogin Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReloginExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Pin  salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun tidak aktif",
                                            value = "{\"code\": 401, \"message\": \"Akun anda tidak aktif\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT Kadaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface ReloginResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Update Mpin Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ForgotMpinExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Password  salah",
                                            value = "{\"code\": 401, \"message\": \"Password yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Mpin  salah",
                                            value = "{\"code\": 401, \"message\": \"MPin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT Kadaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface ForgotMpinResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Mutasi ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"code\": 200,\n" +
                                            "  \"message\": \"Nomor Rekening ditemukan\",\n" +
                                            "  \"status\": true,\n" +
                                            "  \"data\": [\n" +
                                            "    {\n" +
                                            "      \"transactionId\": 84,\n" +
                                            "      \"transactionDate\": \"2024-08-20T05:55:59.398+00:00\",\n" +
                                            "      \"noTransaction\": \"202408201255590002\",\n" +
                                            "      \"destinationNameAccountNumber\": \"BRIVA\",\n" +
                                            "      \"destinationBankName\": \"BCA\",\n" +
                                            "      \"amountTransfer\": {\n" +
                                            "        \"amount\": 50000,\n" +
                                            "        \"currency\": \"IDR\"\n" +
                                            "      },\n" +
                                            "      \"transactionInformation\": \"UANG_KELUAR\",\n" +
                                            "      \"transactionsType\": \"VIRTUAL_ACCOUNT\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"transactionId\": 83,\n" +
                                            "      \"transactionDate\": \"2024-08-20T05:55:24.081+00:00\",\n" +
                                            "      \"noTransaction\": \"202408201255230001\",\n" +
                                            "      \"destinationNameAccountNumber\": \"BCA Virtual Account\",\n" +
                                            "      \"destinationBankName\": \"BRI\",\n" +
                                            "      \"amountTransfer\": {\n" +
                                            "        \"amount\": 100000,\n" +
                                            "        \"currency\": \"IDR\"\n" +
                                            "      },\n" +
                                            "      \"transactionInformation\": \"UANG_KELUAR\",\n" +
                                            "      \"transactionsType\": \"ANTAR_BANK\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor Rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor Rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Transaksi tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface MutasiApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Berhasil download file)",
                    content = {@Content(mediaType = "text/plain", examples = @ExampleObject(value = "File Downloaded"))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor Rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor Rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Transaksi tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface MutasiDownloadResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Berhasil mendapatkan notifikasi)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"code\": 200,\n" +
                                            "  \"message\": \"Data notifkasi ditemukan\",\n" +
                                            "  \"status\": true,\n" +
                                            "  \"data\": [\n" +
                                            "    {\n" +
                                            "      \"createdDate\": \"20 Agustus 2024 12:55 WIB\",\n" +
                                            "      \"typeNotification\": \"Transaksi\",\n" +
                                            "      \"tittle\": \"Transfer ke Virtual Account telah berhasil\",\n" +
                                            "      \"description\": \"Transfer senilai 50000.0 ke rekening 123456789\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"createdDate\": \"20 Agustus 2024 12:55 WIB\",\n" +
                                            "      \"typeNotification\": \"Transaksi\",\n" +
                                            "      \"tittle\": \"Transfer ke Virtual Account telah berhasil\",\n" +
                                            "      \"description\": \"Transfer senilai 100000.0 ke rekening 9876543\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"createdDate\": \"20 Agustus 2024 10:39 WIB\",\n" +
                                            "      \"typeNotification\": \"Transaksi\",\n" +
                                            "      \"tittle\": \"Topup ke DANA telah berhasil\",\n" +
                                            "      \"description\": \"Transfer senilai 12500.0 ke nomor 089123123123\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Transaksi tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface NotificationResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Transfer Intra-Bank Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsCreateExampleSwagger.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request (Nomor rekening tujuan sama dengan nomor rekening pengirim)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"Nomor rekening tujuan tidak boleh sama dengan nomor rekening pengirim\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "MPIN Account salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun terblokir",
                                            value = "{\"code\": 401, \"message\": \"Akun anda terblokir\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT kedaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "402", description = "Payment Required (Saldo tidak cukup)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 402, \"message\": \"Saldo Anda Tidak Cukup\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface TransferIntraCreateApiResponses {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Cek nomor rekening berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsCheckExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Nomor Rekening Tidak Ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 404, \"message\": \"Nomor Rekening Tidak Ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface TransferIntraCheckApiResponses {
    }


    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Transfer Inter-Bank Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsInterCreateExampleSwagger.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request (Nomor rekening tujuan sama dengan nomor rekening pengirim)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"Nomor rekening tujuan tidak boleh sama dengan nomor rekening pengirim\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "MPIN Account salah",
                                            value = "{\"code\": 401, \"message\": \"Pin yang anda masukkan salah\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Akun terblokir",
                                            value = "{\"code\": 401, \"message\": \"Akun anda terblokir\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "JWT kedaluwarsa",
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "402", description = "Payment Required (Saldo tidak cukup)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 402, \"message\": \"Saldo Anda Tidak Cukup\", \"status\": false , \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Nomor rekening tidak ditemukan",
                                            value = "{\"code\": 404, \"message\": \"Nomor rekening tidak ditemukan\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface TransferInterCreateApiResponses {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Cek nomor rekening berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsInterCheckExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Nomor Rekening Tidak Ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 404, \"message\": \"Nomor Rekening Tidak Ditemukan\", \"status\": false , \"data\": null}"
                                    ),
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface TransferInterCheckApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Transaksi terakhir ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"code\": 200,\n" +
                                            "  \"message\": \"History Transaksi tersedia\",\n" +
                                            "  \"status\": true,\n" +
                                            "  \"data\": [\n" +
                                            "    {\n" +
                                            "      \"name_recipient\": \"Jane Smith\",\n" +
                                            "      \"bank_name\": \"Bank BCA\",\n" +
                                            "      \"account_number_recipient\": \"987654321\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Transaksi tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface LastTransactionIntraApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Transaksi terakhir ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"code\": 200,\n" +
                                            "  \"message\": \"History Transaksi tersedia\",\n" +
                                            "  \"status\": true,\n" +
                                            "  \"data\": [\n" +
                                            "    {\n" +
                                            "      \"name_recipient\": \"Fuad\",\n" +
                                            "      \"bank_name\": \"Bank Rakyat Indonesia (BRI)\",\n" +
                                            "      \"account_number_recipient\": \"789012345\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found (Transaksi tidak ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"Transaksi tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface LastTransactionInterApiResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok (Bank ditemukan)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"code\": 200,\n" +
                                            "  \"message\": \"Berhasil mendapatkan data\",\n" +
                                            "  \"status\": true,\n" +
                                            "  \"data\": [\n" +
                                            "    {\n" +
                                            "      \"bank_code\": \"015\",\n" +
                                            "      \"bank_id\": 2,\n" +
                                            "      \"bank_image\": null,\n" +
                                            "      \"bank_name\": \"Bank Rakyat Indonesia (BRI)\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"bank_code\": \"016\",\n" +
                                            "      \"bank_id\": 3,\n" +
                                            "      \"bank_image\": null,\n" +
                                            "      \"bank_name\": \"Bank Negara Indonesia (BNI)\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"bank_code\": \"014\",\n" +
                                            "      \"bank_id\": 1,\n" +
                                            "      \"bank_image\": null,\n" +
                                            "      \"bank_name\": \"Bank Syariah Indonesia (BSI)\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"bank_code\": \"017\",\n" +
                                            "      \"bank_id\": 4,\n" +
                                            "      \"bank_image\": null,\n" +
                                            "      \"bank_name\": \"Bank Mandiri\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized (JWT kedaluwarsa)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"Data bank belum ada\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface BankApiResponses {
    }
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Update Mpin Berhasil)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ForgotMpinExampleSwagger.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Pin app lock  salah",
                                            value = "{\"code\": 400, \"message\": \"Pin App Lock harus 6 digit angka\", \"status\": false , \"data\": null}"
                                    ),
                                    @ExampleObject(
                                            name = "Pin app lock sudah digunakan",
                                            value = "{\"code\": 400, \"message\": \"Pin App Lock sudah digunakan sebelumnya\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface UpdateMpinResponses {
    }

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok (Berhasil mendapatkan detail user)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileExampleSwagger.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\"code\": 401, \"message\": \"JWT Token sudah kedaluwarsa\", \"status\": false , \"data\": null}"
                                    )
                            }

                    )),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"User tidak ditemukan\", \"status\": false , \"data\": null}"
                            )

                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Pesan error yang tidak terhandle)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"example error\", \"status\": false , \"data\": null}"
                            )
                    ))
    })
    public @interface ProfileResponses {
    }
}
 