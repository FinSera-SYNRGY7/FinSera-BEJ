package com.finalproject.finsera.finsera.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.finalproject.finsera.finsera.model.entity.Banks;
import com.finalproject.finsera.finsera.repository.BankRepository;
import com.finalproject.finsera.finsera.service.BankService;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class BankServiceImpl implements BankService{
    @Autowired BankRepository bankRepository;

    public List<?> getListBanks(){
        List<Banks> listBanks = bankRepository.findAll();

        if (listBanks.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data bank belum ada");
        }
        List<Map<String, Object>> dataResponse = new ArrayList<>();
        
        for (Banks bank : listBanks) {
            Map<String, Object> data = new HashMap<>();
            data.put("bank_id", bank.getIdBank());
            data.put("bank_code", bank.getBankCode());
            data.put("bank_image", bank.getBankImage());
            data.put("bank_name", bank.getBankName());
            dataResponse.add(data);
        }
        
        return dataResponse;
    }
    
}
