package com.finalproject.finsera.finsera.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "notifications", schema = "public")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private long idNotification;

    @Column(name = "date_notification")
    private Timestamp dateNotification;

    @Column(name = "text_notification")
    private String textNotification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customers customers;

    private String type;
}
