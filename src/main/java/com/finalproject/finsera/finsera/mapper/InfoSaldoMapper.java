package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.Amount;
import com.finalproject.finsera.finsera.dto.InfoSaldoResponse;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.repository.InfoSaldoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InfoSaldoMapper {

    public InfoSaldoResponse toInfoSaldoResponse(BankAccounts bankAccounts) {
        Amount amount = new Amount();
        amount.setAmount(bankAccounts.getAmount());
        amount.setCurrency("IDR");
        return InfoSaldoResponse.builder()
                .accountNumber(bankAccounts.getAccountNumber())
                .username(bankAccounts.getCustomer().getName())
                .customerId(bankAccounts.getCustomer().getIdCustomers())
                .amount(amount)
                .build();
    };
}
