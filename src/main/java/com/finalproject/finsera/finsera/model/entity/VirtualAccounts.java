package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account_dummy_data", schema = "public")
public class VirtualAccounts extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "mpin_account")
    private String mpinAccount;

    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "saved_account")
    private Boolean savedAccount;
}
