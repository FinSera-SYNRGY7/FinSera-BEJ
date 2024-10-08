package com.finalproject.finsera.finsera.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;

import jakarta.persistence.LockModeType;

@Repository
public interface BankAccountsRepository extends JpaRepository<BankAccounts, Long>{
    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query(value = "SELECT bac.* FROM bank_accounts bac WHERE bac.customer_id = :customerId", nativeQuery = true)
    // List<BankAccounts> findBankAccountsByCustomerId(@Param("customerId") int customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bac FROM BankAccounts bac WHERE bac.customer.idCustomers = :customerId")
    List<BankAccounts> findBankAccountsByCustomerId(@Param("customerId") long customerId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BankAccounts> findByAccountNumber(String account_number);

    @Query(value = "select * from bank_accounts ba where customer_id = :customerId", nativeQuery = true)
    BankAccounts findByCustomerId(Long customerId);
}
