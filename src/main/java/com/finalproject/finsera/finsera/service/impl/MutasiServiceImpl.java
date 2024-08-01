package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.mutasi.MutasiRequestDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;
import com.finalproject.finsera.finsera.mapper.MutasiMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.MutasiService;
import com.finalproject.finsera.finsera.service.ValidationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MutasiServiceImpl implements MutasiService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    MutasiMapper mutasiMapper;

    @Transactional
    @Override
    public List<MutasiResponseDto> getMutasi(
            String username,
            Timestamp startDate,
            Timestamp endDate,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(((page > 0) ? page - 1 : page), size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Transactions> filteredTransactions;

        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found")));

        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(customers.get().getIdCustomers());
        if (bankAccounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Number not found");
        }
        if (bankAccounts.getCustomer() == customers.get()) {
            if(startDate != null && endDate != null) {
                log.info("start and end date: {}");
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDate(startDate, endDate, bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
            } else {
                log.info("else: {}");
                filteredTransactions = transactionRepository.findAllByBankAccounts(bankAccounts, pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Number not found");
        }


        assert filteredTransactions != null;
        if(filteredTransactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not found");
        } else if (filteredTransactions.stream().toList().size() > 10) {
            return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList().subList(0, 10));
        } else {
            return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList());
        }
    }


}
