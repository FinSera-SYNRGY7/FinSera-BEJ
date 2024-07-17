package com.finalproject.finsera.finsera.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "banks", schema = "public")
public class Banks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private long idBank;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_image")
    private String bankImage;

    @Column(name = "delete_at")
    private Timestamp deletedAt;


}
