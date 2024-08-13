package com.finalproject.finsera.finsera.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.EqualsAndHashCode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "banks", schema = "public")
public class Banks extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long idBank;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_image")
    private String bankImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "banks")
    private List<BankAccountsOtherBanks> bankAccountsOtherBanks;

}
