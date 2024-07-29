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
    public List<MutasiResponseDto> getMutasi(String username, MutasiRequestDto mutasiRequestDto, boolean isSevenDays, boolean isOneMonth, boolean isToday, Timestamp startDate, Timestamp endDate, int page, int size) {

        validationService.validate(mutasiRequestDto);

        Pageable pageable = PageRequest.of(((page > 0) ? page - 1 : page), size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Transactions> filteredTransactions = null;

        Optional<Customers> customers = Optional.ofNullable(customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found")));

        BankAccounts bankAccounts = bankAccountsRepository.findByAccountNumber(mutasiRequestDto.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The account number does not match the token"));
        Instant now = Instant.now();
        Timestamp today = Timestamp.from(now.truncatedTo(ChronoUnit.DAYS));
        if (bankAccounts.getCustomer() == customers.get()) {

            if ((isToday ? 1 : 0) + (isOneMonth ? 1 : 0) + (isSevenDays ? 1 : 0) + ((startDate != null && endDate != null) ? 1 : 0) > 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose only one filter");
            } else if(isToday) {
                log.info("today: {}", today);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Timestamp todayStart = new Timestamp(calendar.getTimeInMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                Timestamp todayEnd = Timestamp.valueOf(timestamp.toLocalDateTime());
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDate(todayStart, todayEnd, bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
            } else if (isSevenDays) {
                log.info("sevenday: {}");
                Timestamp sevenDays = Timestamp.from(now.minus(7, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS));
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDate(sevenDays,today, bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

            } else if(isOneMonth) {
                filteredTransactions = transactionRepository.findAllByBankAccountsAndCreatedDateMonth(bankAccounts.getIdBankAccounts(), pageable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
//
            } else if(startDate != null && endDate != null) {
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
        return mutasiMapper.toMutasiResponse(filteredTransactions.stream().toList());
    }
}
