package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.qris.QrisMerchantRequestDto;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantResponseDto;
import com.finalproject.finsera.finsera.dto.qris.QrisResponseDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;

public interface QrisService {

    QrisResponseDto generateQris(String username);

    QrisMerchantResponseDto merchnatQris(long customerId, QrisMerchantRequestDto qrisMerchantRequestDto);


}
