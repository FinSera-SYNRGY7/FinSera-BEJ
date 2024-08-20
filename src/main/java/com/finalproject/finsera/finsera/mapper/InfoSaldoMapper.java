package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import org.springframework.stereotype.Component;

@Component
public class InfoSaldoMapper {

    public InfoSaldoResponse toInfoSaldoResponse(BankAccounts bankAccounts) {
        Amount amount = new Amount();
        amount.setAmount(bankAccounts.getAmount());
        amount.setCurrency("IDR");
        return InfoSaldoResponse.builder()
                .accountNumber(bankAccounts.getAccountNumber())
                .name(bankAccounts.getCustomer().getName())
                .customerId(bankAccounts.getCustomer().getIdCustomers())
                .amount(amount)
                .build();
    };


}
