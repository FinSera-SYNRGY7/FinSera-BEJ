package com.finalproject.finsera.finsera.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "ewallet_accounts", schema = "public")
public class EwalletAccounts extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ewallet_accounts")
    private long idEwalletAccounts;

    @Column(name = "name")
    private String name;

    @Column(name = "ewallet_account_number")
    private String ewalletAccountNumber;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ewallet_id")
    private Ewallet ewallet;
}
