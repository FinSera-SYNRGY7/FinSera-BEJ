package com.finalproject.finsera.finsera.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "ewallet", schema = "public")
public class Ewallet extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ewallet_id")
    private Long idEwallet;

    @Column(name = "ewallet_name")
    private String ewalletName;

    @Column(name = "ewallet_code")
    private String ewalletCode;

    @Column(name = "ewallet_image")
    private String ewalletImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ewallet")
    private List<EwalletAccounts> ewalletAccounts;

}
