package com.finalproject.finsera.finsera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.entity.TransactionsNumber;

@Repository
public interface TransactionNumberRepository extends JpaRepository<TransactionsNumber, Long>{
    
}
