package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletRequest;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletResponse;

public interface EwalletService {

    EwalletResponse createTopUpEwallet(long userId, EwalletRequest ewalletRequest);


}
