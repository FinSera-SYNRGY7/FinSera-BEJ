package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions", schema = "public")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long idTransaction;

    @Column(name = "to_account_number")
    private String toAccountNumber;

    @Column(name = "bank_id")
    private long bankId;

    @Column(name = "amount_transfer")
    private Double amountTransfer;

    private String notes;

    @Column(name = "transaction_type")
    private TransactionsType type;

    @Column(name = "created_at")
    @CurrentTimestamp
    private Timestamp createdAt;



}
