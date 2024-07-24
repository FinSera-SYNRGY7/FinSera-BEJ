package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Banks;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface BankRepository extends JpaRepository<Banks, Long>{
    
    Optional<Banks> findByBankName(String bank_name);
}
