package com.finalproject.finsera.finsera.model.entity;

import com.finalproject.finsera.finsera.model.enums.TransactionInformation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "transactions_ob", schema = "public")
public class TransactionOtherBanks extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_ob_id")
    private Long idTransactionOtherBanks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_accounts")
    private BankAccounts bankAccounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_accounts_ob")
    private BankAccountsOtherBanks bankAccountsOtherBanks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_number_id")
    private TransactionsNumber transactionsNumber;

    @Column(name = "from_account_number")
    private String fromAccountNumber;

    @Column(name = "to_account_number")
    private String toAccountNumber;
    
    @Column(name = "amount_transfer")
    private Double amountTransfer;
    
    private String notes;
    
    @Column(name = "transaction_information")
    private TransactionInformation transactionInformation;
}
