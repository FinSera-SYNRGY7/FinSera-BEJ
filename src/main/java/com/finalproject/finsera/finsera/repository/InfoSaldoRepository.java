package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InfoSaldoRepository extends JpaRepository<BankAccounts, Long> {
    Optional<BankAccounts> findByAccountNumber(String accountNumber);
    Optional<BankAccounts> findByCustomer(Customers customer);
}
