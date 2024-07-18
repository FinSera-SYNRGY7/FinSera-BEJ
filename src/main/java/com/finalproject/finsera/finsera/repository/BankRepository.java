package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Banks;

import org.springframework.data.jpa.repository.JpaRepository;
public interface BankRepository extends JpaRepository<Banks, Long>{
    
    
}
