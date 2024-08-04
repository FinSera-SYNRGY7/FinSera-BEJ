package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccountsOtherBanks;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.BankAccountsOtherBanksRepository;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MutasiMapper {

    @Autowired
    BankAccountsRepository bankAccountsRepository;


    @Autowired
    BankAccountsOtherBanksRepository bankAccountsOtherBanksRepository;

    public List<MutasiResponseDto> toMutasiResponse(List<Transactions> transactions) {
        return transactions.stream()
                .map(this::mapToMutasiResponseDto)
                .collect(Collectors.toList());
    }


    private MutasiResponseDto mapToMutasiResponseDto(Transactions transaction) {
        Amount amount = new Amount();
        amount.setAmount(transaction.getAmountTransfer());
        amount.setCurrency("IDR");
        String toNameAccountNumber = "";
        log.info("tipe transaksi : " + transaction.getType());
        Optional<BankAccounts> bankAccounts = bankAccountsRepository.findByAccountNumber(transaction.getToAccountNumber());
        if(transaction.getType().equals(TransactionsType.SESAMA_BANK) || transaction.getType().equals(TransactionsType.VIRTUAL_ACCOUNT) ) {
            toNameAccountNumber = bankAccounts.get().getCustomer().getName();
            log.info("toAccountNumber 1 : " + toNameAccountNumber);
        } else if (transaction.getType().equals(TransactionsType.TOP_UP_EWALLET)) {
            toNameAccountNumber = "E-WALLET";
        } else {
            log.info("toAccountNumber 2 : " + toNameAccountNumber);
            toNameAccountNumber = bankAccounts.get().getCustomer().getName();
        }
        return MutasiResponseDto.builder()
                .transactionId(transaction.getIdTransaction())
                .noTransaction(transaction.getTransactionsNumber().getTransactionNumber())
                .transactionDate(transaction.getCreatedDate())
                .destinationNameAccountNumber(toNameAccountNumber)
                .amountTransfer(amount)
                .transactionInformation(transaction.getTransactionInformation())
                .transactionsType(transaction.getType())
                .build();
    }


}
