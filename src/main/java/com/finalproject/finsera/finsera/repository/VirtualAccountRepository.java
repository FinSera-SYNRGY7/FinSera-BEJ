package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.VirtualAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccounts, Long> {
    Optional<VirtualAccounts> findByVirtualAccountNumber(String virtualAccountNumber);
}
