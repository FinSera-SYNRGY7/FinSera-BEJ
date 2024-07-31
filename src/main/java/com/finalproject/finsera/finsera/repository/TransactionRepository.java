package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long>{

    Optional<Page<Transactions>> findAllByBankAccounts(BankAccounts fromBankAccounts, Pageable pageable);

//    @Query("SELECT t FROM Transactions t WHERE (t.bankAccounts.idBankAccounts=:bankAccounts) AND (DATE_TRUNC('month', t.createdDate)=DATE_TRUNC('month', CURRENT_DATE))")
//    Optional<Page<Transactions>> findAllByBankAccountsAndCreatedDateMonth(
//            @Param("bankAccounts") long bankAccounts,
//            Pageable pageable
//    );

    @Query("SELECT t FROM Transactions t WHERE  (t.bankAccounts.idBankAccounts=:bankAccounts) AND" +
            "(t.createdDate BETWEEN :startDate AND :endDate) ")
    Optional<Page<Transactions>> findAllByBankAccountsAndCreatedDate(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("bankAccounts") long bankAccounts,
            Pageable pageable
    );

    @Query(value = "select distinct * from \"transaction\" t where transaction_information = 0 \n" +
            "order by created_date desc limit 3;"
            , nativeQuery = true)
    List<Transactions> getLastAccountTransaction();
}
