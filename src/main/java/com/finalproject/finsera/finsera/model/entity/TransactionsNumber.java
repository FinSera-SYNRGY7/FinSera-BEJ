package com.finalproject.finsera.finsera.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "transactions_number", schema = "public")
public class TransactionsNumber extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_number_id")
    private long idTransactionNumber;

    @Column(name = "transaction_number", unique = true)
    private String transactionNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionsNumber")
    private List<Transactions> transactions;

}