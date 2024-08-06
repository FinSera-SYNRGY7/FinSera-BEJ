package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletCheckResponse;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletRequest;
import com.finalproject.finsera.finsera.dto.ewallet.GetAllEwalletResponse;
import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EwalletMapper {


    public EwalletCheckResponse toEwalletResponse(EwalletAccounts ewalletAccount) {
        return EwalletCheckResponse
                .builder()
                .ewalletName(ewalletAccount.getEwallet().getEwalletName())
                .ewalletAccountName(ewalletAccount.getName())
                .ewalletId(ewalletAccount.getIdEwalletAccounts())
                .ewalletAccount(ewalletAccount.getEwalletAccountNumber())
                .build();
    };

    public GetAllEwalletResponse mapToGetAllEwalletResponse(Ewallet ewallet) {
        return GetAllEwalletResponse
                .builder()
                .ewalletId(ewallet.getIdEwallet())
                .ewalletName(ewallet.getEwalletName())
                .ewalletImage(ewallet.getEwalletImage())
                .build();
    };

    public List<GetAllEwalletResponse> toGetAllEwalletResponse(List<Ewallet> ewallet) {
        return ewallet.stream()
                .map(this::mapToGetAllEwalletResponse)
                .collect(Collectors.toList());
    }
}
