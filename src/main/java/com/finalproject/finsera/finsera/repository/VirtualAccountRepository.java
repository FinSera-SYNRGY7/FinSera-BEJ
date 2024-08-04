package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccounts, Long> {
    VirtualAccounts findByAccountNumber(String accountNumber);
}
