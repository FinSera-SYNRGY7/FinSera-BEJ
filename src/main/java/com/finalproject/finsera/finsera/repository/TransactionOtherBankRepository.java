package com.finalproject.finsera.finsera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finalproject.finsera.finsera.model.entity.TransactionOtherBanks;

@Repository
public interface TransactionOtherBankRepository extends JpaRepository<TransactionOtherBanks, Long>{
    @Query(value = "SELECT DISTINCT ON (t.from_account_number) t.* " +
                   "FROM transactions_ob t " +
                   "WHERE t.to_account_number = :toAccountNumber " +
                   "ORDER BY t.from_account_number, t.created_date DESC LIMIT 4", 
           nativeQuery = true)
    List<TransactionOtherBanks> findDistinctByToAccountNumber(@Param("toAccountNumber") String toAccountNumber);       
}
