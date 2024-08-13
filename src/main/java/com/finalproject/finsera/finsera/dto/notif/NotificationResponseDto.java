package com.finalproject.finsera.finsera.dto.notif;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponseDto {
    private String createdDate;
    private String typeNotification;
    private String tittle;
    private String description;
}
