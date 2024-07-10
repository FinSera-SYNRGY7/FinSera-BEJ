package com.finalproject.finsera.finsera.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "otp", schema = "public")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private long idOtp;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customers customers;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
