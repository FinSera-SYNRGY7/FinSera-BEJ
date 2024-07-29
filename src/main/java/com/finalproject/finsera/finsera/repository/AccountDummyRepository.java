package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDummyRepository extends JpaRepository<AccountDummyData, Long> {
    AccountDummyData findByAccountNumber(String accountNumber);
}
