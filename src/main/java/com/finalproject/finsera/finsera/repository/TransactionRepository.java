package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
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
    Optional<List<Transactions>> findAllByBankAccountsOrderByCreatedDateDesc(BankAccounts bankAccounts);

    @Query(value =
            "SELECT * FROM (" +
                    "    SELECT DISTINCT ON (to_account_number) * " +
                    "    FROM \"transaction\" " +
                    "    WHERE from_account_number = :sender AND transaction_type = :type " +
                    "    ORDER BY to_account_number, created_date DESC" +
                    ") AS unique_transactions " +
                    "ORDER BY created_date DESC " +
                    "LIMIT 4",
            nativeQuery = true)
    Optional<List<Transactions>> getAllHistoryByToAccountNumber(@Param("sender") String sender, @Param("type") TransactionsType type);

//    @Query("SELECT t FROM Transactions t WHERE (t.bankAccounts.idBankAccounts=:bankAccounts) AND (DATE_TRUNC('month', t.createdDate)=DATE_TRUNC('month', CURRENT_DATE))")
//    Optional<Page<Transactions>> findAllByBankAccountsAndCreatedDateMonth(
//            @Param("bankAccounts") long bankAccounts,
//            Pageable pageable
//    );

    @Query("SELECT t FROM Transactions t WHERE  (t.bankAccounts.idBankAccounts=:bankAccounts) AND" +
            "(t.createdDate BETWEEN :startDate AND :endDate) ")
    Optional<Page<Transactions>> findAllByBankAccountsAndCreatedDateWithPage(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("bankAccounts") long bankAccounts,
            Pageable pageable
    );


    @Query("SELECT t FROM Transactions t WHERE  (t.bankAccounts.idBankAccounts=:bankAccounts) AND" +
            "(t.createdDate BETWEEN :startDate AND :endDate) ORDER BY t.createdDate DESC")
    Optional<List<Transactions>> findAllByBankAccountsAndCreatedDate(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("bankAccounts") long bankAccounts
    );

    @Query(value = "select * from \"transaction\" t where transaction_information = 0 \n" +
            "order by created_date;"
            , nativeQuery = true)
    List<Transactions> getLastAccountTransactionVA();

//    @Query(value = "SELECT DISTINCT ON (t.from_account_number) t.* " +
//                   "FROM transaction t " +
//                   "WHERE t.to_account_number = :toAccountNumber " +
//                   "ORDER BY t.from_account_number, t.created_date DESC LIMIT 4",
//           nativeQuery = true)
//    List<Transactions> findDistinctByToAccountNumber(@Param("toAccountNumber") String toAccountNumber);

    @Query(value = "SELECT DISTINCT ON (t.from_account_number) t.* " +
            "FROM public.transaction t " +
            "WHERE t.to_account_number = :toAccountNumber AND t.transaction_type = :transaksiType " +
            "ORDER BY t.from_account_number, t.created_date DESC LIMIT 4",
            nativeQuery = true)
    List<Transactions> findDistinctByToAccountNumber(@Param("toAccountNumber") String toAccountNumber, @Param("transaksiType") TransactionsType transactionsType);


}
