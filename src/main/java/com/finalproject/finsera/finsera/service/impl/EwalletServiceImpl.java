package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletCheckAccountRequest;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletCheckResponse;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletRequest;
import com.finalproject.finsera.finsera.dto.ewallet.GetAllEwalletResponse;
import com.finalproject.finsera.finsera.mapper.EwalletMapper;
import com.finalproject.finsera.finsera.model.entity.Banks;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import com.finalproject.finsera.finsera.repository.EwalletAccountsRepository;
import com.finalproject.finsera.finsera.repository.EwalletRepository;
import com.finalproject.finsera.finsera.service.EwalletService;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EwalletServiceImpl implements EwalletService {


    @Autowired
    EwalletAccountsRepository ewalletAccountsRepository;

    @Autowired
    EwalletRepository ewalletRepository;

    @Autowired
    EwalletMapper ewalletMapper;

    @Override
    public EwalletCheckResponse checkAccountEwallet(EwalletCheckAccountRequest ewalletCheckAccountRequest) {

        Optional<Ewallet> ewallet = Optional.ofNullable(ewalletRepository.findById(ewalletCheckAccountRequest.getEwalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "E-wallet tidak ditemukkan")));
        Optional<EwalletAccounts> ewalletAccount = Optional.ofNullable(ewalletAccountsRepository.findByEwalletAndEwalletAccountNumber(ewallet.get(), ewalletCheckAccountRequest.getEwalletAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor e-wallet tidak ditemukkan")));

        return ewalletMapper.toEwalletResponse(ewalletAccount.get());
    }

    @Override
    public List<GetAllEwalletResponse> getAllEwallet() {
        List<Ewallet> listEwallets = ewalletRepository.findAll();

        if(listEwallets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ewallet tidak ditemukkan");
        }
        return ewalletMapper.toGetAllEwalletResponse(listEwallets);
    }

}
