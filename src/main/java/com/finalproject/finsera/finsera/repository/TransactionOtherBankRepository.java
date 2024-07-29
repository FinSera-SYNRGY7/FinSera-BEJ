package com.finalproject.finsera.finsera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.finsera.finsera.model.entity.TransactionOtherBanks;
import com.finalproject.finsera.finsera.model.entity.Transactions;

@Repository
public interface TransactionOtherBankRepository extends JpaRepository<TransactionOtherBanks, Long>{
    
}
