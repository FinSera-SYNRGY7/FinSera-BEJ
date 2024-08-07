package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccountsOtherBanks;
import com.finalproject.finsera.finsera.model.entity.Ewallet;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EwalletAccountsRepository extends JpaRepository<EwalletAccounts, Long>{

    Optional<EwalletAccounts> findByEwalletAndEwalletAccountNumber(Ewallet ewallet, String ewalletAcccountNumber);

    Optional<EwalletAccounts> findByEwalletAccountNumber(String ewalletAccountNumber);

}
