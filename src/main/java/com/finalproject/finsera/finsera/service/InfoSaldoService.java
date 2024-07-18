package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.InfoSaldoRequest;
import com.finalproject.finsera.finsera.dto.InfoSaldoResponse;

import java.util.Map;

public interface InfoSaldoService {
    InfoSaldoResponse getInfoSaldo(InfoSaldoRequest infoSaldoRequest);
}
