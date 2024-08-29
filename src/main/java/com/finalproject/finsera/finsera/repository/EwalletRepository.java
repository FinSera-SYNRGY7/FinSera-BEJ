package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.Banks;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EwalletRepository extends JpaRepository<Ewallet, Long>{


}
