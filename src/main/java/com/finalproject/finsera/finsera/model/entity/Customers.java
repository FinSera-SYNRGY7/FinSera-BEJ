package com.finalproject.finsera.finsera.model.entity;


import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.List;

@Entity
@Data
@Table(name = "customers", schema = "public")
@EqualsAndHashCode(callSuper = true)
public class Customers extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long idCustomers;

    private String address;

    private Gender gender;

    @Column(name = "father_user")
    private String fatherName;

    @Column(name = "mother_user")
    private String motherName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Double income;

    private String username;

    private String name;

    private String nik;

    private String password;

    private String email;

    @Column(name = "mpin_auth")
    private String mpinAuth;

    @Column(name = "status_user")
    private StatusUser statusUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<BankAccounts> bankAccounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Notifications> notifications;

}
