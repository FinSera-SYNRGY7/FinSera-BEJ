package com.finalproject.finsera.finsera.model.entity;


import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long idCustomers;

    private String name;

    private String nik;

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

    private String password;

    private String email;

    private String mpin;

    @Column(name = "status_user")
    private StatusUser statusUser;

    @Column(name = "delete_at")
    private Timestamp deletedAt;

    @Column(name = "created_at")
    @CurrentTimestamp
    private Timestamp createdAt;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public StatusUser getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(StatusUser statusUser) {
        this.statusUser = statusUser;
    }
}
