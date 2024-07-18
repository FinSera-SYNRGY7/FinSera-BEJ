package com.finalproject.finsera.finsera.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "bank_accounts", schema = "public")
public class BankAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long idBankAccounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customers customer;

    @Column(name = "accout_number")
    private String accountNumber;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "delete_at")
    private Timestamp deletedAt;
}
