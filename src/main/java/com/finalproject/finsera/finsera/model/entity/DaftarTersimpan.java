package com.finalproject.finsera.finsera.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "daftar_tersimpan", schema = "public")
public class DaftarTersimpan extends BaseModel {

    @Id
    @Column(name = "id_daftar_tersimpan")
    private long idDaftarTersimpan;


    


}
