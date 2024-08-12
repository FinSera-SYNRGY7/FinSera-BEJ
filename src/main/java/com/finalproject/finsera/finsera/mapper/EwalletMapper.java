package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.ewallet.*;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EwalletMapper {

    public EwalletResponse toCreateTransactionEwalletResponse(
            Optional<EwalletAccounts> ewalletAccount,
            BankAccounts bankAccounts,
            EwalletRequest ewalletRequest,
            Transactions transactions
    ) {
        return EwalletResponse
                .builder()
                .ewalletName(ewalletAccount.get().getEwallet().getEwalletName())
                .ewalletAccountName(ewalletAccount.get().getName())
                .ewalletAccount(ewalletAccount.get().getEwalletAccountNumber())
                .accountSender(bankAccounts.getAccountNumber())
                .nominal(DateFormatterIndonesia.formatCurrency(ewalletRequest.getNominal()))
                .nameSender(bankAccounts.getCustomer().getName())
                .transactionDate(DateFormatterIndonesia.dateFormatterIND(transactions.getCreatedDate()))
                .transactionNum(transactions.getTransactionsNumber().getTransactionNumber())
                .note(ewalletRequest.getNote())
                .build();
    };


    public EwalletCheckResponse toEwalletResponse(EwalletAccounts ewalletAccount) {
        return EwalletCheckResponse
                .builder()
                .ewalletName(ewalletAccount.getEwallet().getEwalletName())
                .ewalletAccountName(ewalletAccount.getName())
                .ewalletAccountId(ewalletAccount.getIdEwalletAccounts())
                .ewalletAccount(ewalletAccount.getEwalletAccountNumber())
                .build();
    };



    public List<GetAllEwalletResponse> toGetAllEwalletResponse(List<Ewallet> ewallet) {
        return ewallet.stream()
                .map(this::mapToGetAllEwalletResponse)
                .collect(Collectors.toList());
    }


    public GetAllEwalletResponse mapToGetAllEwalletResponse(Ewallet ewallet) {
        return GetAllEwalletResponse
                .builder()
                .ewalletId(ewallet.getIdEwallet())
                .ewalletName(ewallet.getEwalletName())
                .ewalletImage(ewallet.getEwalletImage())
                .build();
    };

    public List<EwalletHistoryResponse> toGetAllHistoryEwalletResponse(List<EwalletAccounts> ewalletAccounts) {
        return ewalletAccounts.stream()
                .map(this::mapToGetAllHistoryEwalletResponse)
                .collect(Collectors.toList());
    }

    public EwalletHistoryResponse mapToGetAllHistoryEwalletResponse(EwalletAccounts ewalletAccounts) {
        return EwalletHistoryResponse
                .builder()
                .ewalletAccountName(ewalletAccounts.getName())
                .ewalletName(ewalletAccounts.getEwallet().getEwalletName())
                .ewalletAccountId(ewalletAccounts.getIdEwalletAccounts())
                .ewalletAccount(ewalletAccounts.getEwalletAccountNumber())
                .ewalletImage(ewalletAccounts.getEwallet().getEwalletImage())
                .build();
    };
}
