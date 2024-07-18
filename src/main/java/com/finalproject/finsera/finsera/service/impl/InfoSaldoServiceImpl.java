package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.mapper.InfoSaldoMapper;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.repository.InfoSaldoRepository;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import com.finalproject.finsera.finsera.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class InfoSaldoServiceImpl implements InfoSaldoService {


    private final InfoSaldoRepository infoSaldoRepository;
    private final ValidationService validationService;
    private final InfoSaldoMapper infoSaldoMapper;


    @Override
    public InfoSaldoResponse getInfoSaldo(InfoSaldoRequest infoSaldoRequest) {
        validationService.validate(infoSaldoRequest);
        Optional<BankAccounts> bankAccounts = Optional.ofNullable(infoSaldoRepository.findByAccountNumber(infoSaldoRequest.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Rekening Tidak Ditemukan")));
        return infoSaldoMapper.toInfoSaldoResponse(bankAccounts.get());
    }
}
