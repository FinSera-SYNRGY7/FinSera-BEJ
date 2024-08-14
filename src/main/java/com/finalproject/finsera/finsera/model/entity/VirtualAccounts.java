package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "virtual_account", schema = "public")
public class VirtualAccounts extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String virtualAccountNumber;

    @Column(name = "nominal")
    private Double nominal;

    @Column(name = "account_type")
    private AccountType accountType;
}
