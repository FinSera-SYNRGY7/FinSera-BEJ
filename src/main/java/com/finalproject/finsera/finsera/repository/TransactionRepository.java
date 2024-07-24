package com.finalproject.finsera.finsera.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.finsera.finsera.model.entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long>{
    
}
