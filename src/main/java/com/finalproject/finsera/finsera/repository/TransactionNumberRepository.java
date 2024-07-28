package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.TransactionsNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionNumberRepository extends JpaRepository<TransactionsNumber, Long> {
}
