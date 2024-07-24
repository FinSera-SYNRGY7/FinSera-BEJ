package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.TransactionsByBankType;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions", schema = "public")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long idTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccounts bankAccounts;

    @Column(name = "to_account_number")
    private String toAccountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Banks banks;

    @Column(name = "amount_transfer")
    private Double amountTransfer;

    private String notes;

    @Column(name = "transaction_type")
    private TransactionsType type;

    @Column(name = "transaction_by_bank_type")
    private TransactionsByBankType byBankType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = true, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = true)
    @LastModifiedDate
    private Date updatedDate;

}