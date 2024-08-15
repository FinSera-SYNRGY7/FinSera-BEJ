package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.BankAccountsOtherBanksRepository;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.EwalletAccountsRepository;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    EwalletAccountsRepository ewalletAccountsRepository;

    public List<MutasiResponseDto> toMutasiResponse(List<Transactions> transactions) {
        return transactions.stream()
                .map(this::mapToMutasiResponseDto)
                .collect(Collectors.toList());
    }


    private MutasiResponseDto mapToMutasiResponseDto(Transactions transaction) {
        Amount amount = new Amount();
        amount.setAmount(transaction.getAmountTransfer());
        amount.setCurrency("IDR");
        String toNameAccountNumber;
        String toBankName;
        Optional<BankAccounts> bankAccounts = bankAccountsRepository.findByAccountNumber(transaction.getToAccountNumber());
        if (transaction.getType().equals(TransactionsType.SESAMA_BANK)) {
            toNameAccountNumber = bankAccounts.get().getCustomer().getName();
            toBankName = "BCA";
        } else if (transaction.getType().equals(TransactionsType.VIRTUAL_ACCOUNT)) {
            VirtualAccounts virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(transaction.getToAccountNumber());
            toNameAccountNumber = virtualAccounts.getAccountName();
            toBankName = "BCA";
        } else if (transaction.getType().equals(TransactionsType.TOP_UP_EWALLET)) {
            Optional<EwalletAccounts> ewalletAccounts = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAccountNumber(transaction.getToAccountNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet account not found")));
            toNameAccountNumber = ewalletAccounts.get().getName();
            toBankName = ewalletAccounts.get().getEwallet().getEwalletName();
        } else {
            Optional<BankAccountsOtherBanks> bankAccountsOtherBanks = Optional.ofNullable(bankAccountsOtherBanksRepository.findByAccountNumber(transaction.getToAccountNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found")));
            toNameAccountNumber = bankAccountsOtherBanks.get().getName();
            toBankName = bankAccountsOtherBanks.get().getBanks().getBankName();
        }
        return MutasiResponseDto.builder()
                .transactionId(transaction.getIdTransaction())
                .noTransaction(transaction.getTransactionsNumber().getTransactionNumber())
                .transactionDate(transaction.getCreatedDate())
                .destinationNameAccountNumber(toNameAccountNumber)
                .destinationBankName(toBankName)
                .amountTransfer(amount)
                .transactionInformation(transaction.getTransactionInformation())
                .transactionsType(transaction.getType())
                .build();
        }

    }
