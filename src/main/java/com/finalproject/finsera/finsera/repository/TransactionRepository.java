package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.dto.accountDummy.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.model.entity.AccountDummyData;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
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

    @Query(value = "select add2.account_name, t.to_account_number from transactions t\n" +
            "join account_dummy_data add2 on add2.account_number = t.to_account_number \n" +
            "where t.created_date >= now() - interval '3 days' and t.transaction_type = 2", nativeQuery = true)
    List<AccountLastTransactionResponseDto> findByCreatedDate();
}
