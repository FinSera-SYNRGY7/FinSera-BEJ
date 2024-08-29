package com.finalproject.finsera.finsera.model.entity;

import lombok.*;

import java.util.List;

import jakarta.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bank_accounts_ob", schema = "public")
public class BankAccountsOtherBanks extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bank_accounts_ob")
    private Long idBankAccounts;

    @Column(name = "name")
    private String name;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Banks banks;
}
