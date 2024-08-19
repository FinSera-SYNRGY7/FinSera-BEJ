package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.mapper.InfoSaldoMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.repository.InfoSaldoRepository;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import com.finalproject.finsera.finsera.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class InfoSaldoServiceImpl implements InfoSaldoService {


    private final InfoSaldoRepository infoSaldoRepository;
    private final CustomerRepository customersRepository;
    private final ValidationService validationService;
    private final InfoSaldoMapper infoSaldoMapper;


    @Override
    public InfoSaldoResponse getInfoSaldo(String username) {
        validationService.validate(username);

        Customers customers = customersRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Tidak Ditemukan"));

        Optional<BankAccounts> bankAccounts = Optional.ofNullable(infoSaldoRepository.findByCustomer(customers)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening Tidak Ditemukan")));
        return infoSaldoMapper.toInfoSaldoResponse(bankAccounts.get());
    }
}
