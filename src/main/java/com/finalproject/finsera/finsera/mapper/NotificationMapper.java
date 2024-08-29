package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.ewallet.*;
import com.finalproject.finsera.finsera.dto.notif.NotificationResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.EwalletAccountsRepository;
import com.finalproject.finsera.finsera.repository.EwalletRepository;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import jakarta.persistence.Access;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotificationMapper {


    @Autowired
    EwalletAccountsRepository ewalletAccountsRepository;

    public List<NotificationResponseDto> toNotificationsResponse(List<Transactions> notificationResponseDto) {
        return notificationResponseDto.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponseDto mapToNotificationResponse(Transactions transactions) {
        String typeNotif = "Transaksi";
        String decription;
        String tittle;
        String createdDate = DateFormatterIndonesia.otherDateFormatterIND(transactions.getCreatedDate());
        if(transactions.getType() == TransactionsType.SESAMA_BANK) {
            if(transactions.getTransactionInformation() == TransactionInformation.UANG_KELUAR) {
                tittle = "Transfer ke sesama bank telah berhasil";
                decription = "Transfer senilai " + transactions.getAmountTransfer() + " ke rekening " + transactions.getToAccountNumber();
            } else {
                tittle = "Penerimaan ke sesama bank telah berhasil";
                decription = "Penerimaan senilai " + transactions.getAmountTransfer() + " dari rekening " + transactions.getToAccountNumber();
            }
        } else if(transactions.getType() == TransactionsType.ANTAR_BANK) {
            tittle = "Transfer ke antar bank telah berhasil";
            decription = "Transfer senilai " + transactions.getAmountTransfer() + " ke rekening " + transactions.getToAccountNumber();
        } else if(transactions.getType() == TransactionsType.TOP_UP_EWALLET) {
            Optional<EwalletAccounts> ewalletAccounts = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAccountNumber(transactions.getToAccountNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet account not found")));
            tittle = "Topup ke " + ewalletAccounts.get().getEwallet().getEwalletName() + " telah berhasil";
            decription = "Transfer senilai " + transactions.getAmountTransfer() + " ke nomor " + transactions.getToAccountNumber();
        } else {
            if(transactions.getTransactionInformation() == TransactionInformation.UANG_KELUAR) {
                tittle = "Transfer ke Virtual Account telah berhasil";
                decription = "Transfer senilai " + transactions.getAmountTransfer() + " ke rekening " + transactions.getToAccountNumber();
            } else {
                tittle = "Penerimaan ke Virtual Account telah berhasil";
                decription = "Penerimaan senilai " + transactions.getAmountTransfer() + " dari rekening " + transactions.getToAccountNumber();
            }
        }

        return NotificationResponseDto
                .builder()
                .typeNotification(typeNotif)
                .createdDate(createdDate)
                .description(decription)
                .tittle(tittle)
                .build();
    };
}
