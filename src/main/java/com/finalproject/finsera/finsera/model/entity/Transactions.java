package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsByBankType;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "transaction", schema = "public")
public class Transactions extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long idTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_accounts")
    private BankAccounts bankAccounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_number_id")
    private TransactionsNumber transactionsNumber;

    @Column(name = "from_account_number")
    private String fromAccountNumber;

    @Column(name = "to_account_number")
    private String toAccountNumber;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "bank_id")
//     private Banks banks;

    @Column(name = "amount_transfer")
    private Double amountTransfer;

    private String notes;

    @Column(name = "transaction_type")
    private TransactionsType type;

    @Column(name = "transaction_information")
    private TransactionInformation transactionInformation;
}