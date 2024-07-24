package com.finalproject.finsera.finsera.model.entity;


import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.List;

@Entity
@Data
@Table(name = "customers", schema = "public")
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long idCustomers;

    private String address;

    @Column(name = "created_at")
    @CurrentTimestamp
    private Timestamp createdAt;

    @Column(name = "delete_at")
    private Timestamp deletedAt;

    private String email;

    @Column(name = "father_user")
    private String fatherName;

    private Gender gender;

    @Column(name = "mother_user")
    private String motherName;

    private Double income;

    private String mpin;

    private String name;

    private String nik;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status_user")
    private StatusUser statusUser;

    private String username;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<BankAccounts> bankAccounts;


}
