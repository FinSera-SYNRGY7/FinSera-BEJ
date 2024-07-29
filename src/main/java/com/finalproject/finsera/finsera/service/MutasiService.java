package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.mutasi.MutasiRequestDto;
import com.finalproject.finsera.finsera.dto.mutasi.MutasiResponseDto;

import java.sql.Timestamp;
import java.util.List;

public interface MutasiService {

    List<MutasiResponseDto> getMutasi(
            String username,
            MutasiRequestDto mutasiRequestDto,
            boolean isSevenDays,
            boolean isOneMonth,
            boolean isToday,
            Timestamp startDate,
            Timestamp endDate,
            int page,
            int size);
}
