package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.infosaldo.InfoSaldoResponse;
import com.finalproject.finsera.finsera.mapper.InfoSaldoMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomersRepository;
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
    private final CustomersRepository customersRepository;
    private final ValidationService validationService;
    private final InfoSaldoMapper infoSaldoMapper;


    @Override
    public InfoSaldoResponse getInfoSaldo(InfoSaldoRequest infoSaldoRequest) {
        validationService.validate(infoSaldoRequest);
        Customers customers = customersRepository.findById(infoSaldoRequest.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Tidak Ditemukan"));

        Optional<BankAccounts> bankAccounts = Optional.ofNullable(infoSaldoRepository.findByCustomer(customers)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening Tidak Ditemukan")));
        return infoSaldoMapper.toInfoSaldoResponse(bankAccounts.get());
    }
}
