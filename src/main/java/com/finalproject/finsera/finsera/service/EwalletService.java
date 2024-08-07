package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.ewallet.*;
import com.finalproject.finsera.finsera.model.entity.Ewallet;

import java.util.List;

public interface EwalletService {

    EwalletResponse createEwalletTransactions(long idCustomers, EwalletRequest ewalletRequest);

    EwalletCheckResponse checkAccountEwallet(EwalletCheckAccountRequest ewalletCheckAccountRequest);

    List<GetAllEwalletResponse> getAllEwallet();

    List<EwalletHistoryResponse> historyTransactionEwallet(long idCustomers);

}
