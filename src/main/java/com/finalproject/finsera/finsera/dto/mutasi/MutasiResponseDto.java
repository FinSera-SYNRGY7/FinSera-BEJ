package com.finalproject.finsera.finsera.dto.mutasi;

import com.finalproject.finsera.finsera.dto.infosaldo.Amount;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class MutasiResponseDto {
    private long transactionId;

    private Date transactionDate;

    private String noTransaction;

    private String destinationNameAccountNumber;

    private String destinationBankName;

    private Amount amountTransfer;

    private TransactionInformation transactionInformation;

    private TransactionsType transactionsType;

}
