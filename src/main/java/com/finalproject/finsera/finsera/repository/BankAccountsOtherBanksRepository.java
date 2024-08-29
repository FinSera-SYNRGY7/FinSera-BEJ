package com.finalproject.finsera.finsera.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.BankAccountsOtherBanks;


@Repository
public interface BankAccountsOtherBanksRepository extends JpaRepository<BankAccountsOtherBanks, Long>{
    @Query(value = "SELECT bac_ob.* FROM bank_accounts_ob bac_ob WHERE bac_ob.account_number = :accountNumber AND bac_ob.bank_id = :bankId", nativeQuery = true)
    List<BankAccountsOtherBanks> findBankAccountsByAccountNumberAndBankId(@Param("accountNumber") String accountNumber, @Param("bankId") int bankId);
    
    Optional<BankAccountsOtherBanks> findByAccountNumber(String account_number);

}
