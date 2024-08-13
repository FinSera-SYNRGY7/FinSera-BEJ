package com.finalproject.finsera.finsera.service;

import com.finalproject.finsera.finsera.dto.notif.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    List<NotificationResponseDto> getNotif(Long userId);
}
