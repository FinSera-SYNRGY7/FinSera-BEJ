package com.finalproject.finsera.finsera.mapper;

import com.finalproject.finsera.finsera.dto.ewallet.EwalletRequest;
import com.finalproject.finsera.finsera.dto.ewallet.EwalletResponse;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantRequestDto;
import com.finalproject.finsera.finsera.dto.qris.QrisMerchantResponseDto;
import com.finalproject.finsera.finsera.dto.transaction.TransactionResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.EwalletAccounts;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class QrisMapper {

    public QrisMerchantResponseDto toCreateTransactionQrisResponse(
            BankAccounts bankAccounts,
            QrisMerchantRequestDto qrisMerchantRequestDto,
            Transactions transactions
    ) {
        return QrisMerchantResponseDto
                .builder()
                .transaction_num(transactions.getTransactionsNumber().getTransactionNumber())
                .transaction_date(DateFormatterIndonesia.dateFormatterIND(transactions.getCreatedDate()))
                .name_recipient(qrisMerchantRequestDto.getMerchantName())
                .accountnum_recipient(qrisMerchantRequestDto.getMerchantNo())
                .accountnum_sender(bankAccounts.getAccountNumber())
                .name_sender(bankAccounts.getCustomer().getName())
                .nominal(DateFormatterIndonesia.formatCurrency(qrisMerchantRequestDto.getNominal()))
                .build();
    };
}
