package com.finalproject.finsera.finsera.model.entity;


import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "customers", schema = "public")
@EqualsAndHashCode(callSuper = true)
public class Customers extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long idCustomers;

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

    @Column(name = "failed_attempt")
    private Integer failedAttempt;

    @Column(name = "banned_time")
    private Date bannedTime;

}
