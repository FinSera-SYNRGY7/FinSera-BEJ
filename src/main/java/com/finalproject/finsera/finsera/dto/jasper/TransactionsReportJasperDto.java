package com.finalproject.finsera.finsera.dto.jasper;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class TransactionsReportJasperDto {
    private Date created_date;
    private String notes;
    private Integer transaction_information;
    private Double amount_transfer;
}
