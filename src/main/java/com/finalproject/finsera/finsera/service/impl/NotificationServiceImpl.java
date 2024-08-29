package com.finalproject.finsera.finsera.service.impl;


import com.finalproject.finsera.finsera.dto.customer.CustomerResponse;
import com.finalproject.finsera.finsera.dto.notif.NotificationResponseDto;
import com.finalproject.finsera.finsera.mapper.NotificationMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceImpl  implements NotificationService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Autowired
    NotificationMapper notificationMapper;
    @Override
    public List<NotificationResponseDto> getNotif(Long userId) {
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan")));


        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if(bankAccounts == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor rekening tidak ditemukan");
        }

        List<Transactions> transactions = transactionRepository.findAllByBankAccountsOrderByCreatedDateDesc(bankAccounts)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"));

        if(transactions.size() <= 10) {
            return notificationMapper.toNotificationsResponse(transactions);
        } else {
            return notificationMapper.toNotificationsResponse(transactions.subList(0, 10));
        }
    }
}
