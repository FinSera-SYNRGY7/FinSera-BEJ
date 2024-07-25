package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MutasiMapper {

    public List<MutasiResponseDto> toMutasiResponse(List<Transactions> transactions) {
        return transactions.stream()
                .map(this::mapToMutasiResponseDto)
                .collect(Collectors.toList());
    }


    private MutasiResponseDto mapToMutasiResponseDto(Transactions transaction) {
        Amount amount = new Amount();
        amount.setAmount(transaction.getAmountTransfer());
        amount.setCurrency("IDR");

        return MutasiResponseDto.builder()
                .transactionId(transaction.getIdTransaction())
                .noTransaction(transaction.getNoTransaction())
                .transactionDate(transaction.getCreatedDate())
                .accountNumber(transaction.getFromAccountNumber())
                .amountTransfer(amount)
                .transactionInformation(transaction.getTransactionInformation())
                .build();
    }


}
