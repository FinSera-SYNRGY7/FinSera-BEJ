package com.finalproject.finsera.finsera.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finalproject.finsera.finsera.dto.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionResponseDto;
import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Banks;
import com.finalproject.finsera.finsera.model.entity.Transactions;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.BankRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.util.InsufficientBalanceException;

import java.util.Optional;
import java.util.Random;
import java.util.Locale;
import java.util.List;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired TransactionRepository transactionRepository;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @Autowired BankRepository bankRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public TransactionResponseDto create(TransactionRequestDto transactionRequestDto){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(transactionRequestDto.getId_user());
        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber( transactionRequestDto.getAccountnum_recipient());
        if (!optionalBankAccountsReceiver.isPresent()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        // Optional<Banks> optionalBanks = bankRepository.findById(12L);
        Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        if (!optionalBanks.isPresent()){
            throw new IllegalArgumentException("Bank Tidak Ditemukan");
        }
        Banks banks = optionalBanks.get();
        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();
        // if (!(bankAccountsSender.getCustomer().getMpin().equals(transactionRequestDto.getPin()))){
        //     
        // }

        if (!(passwordEncoder.matches(transactionRequestDto.getPin(), bankAccountsSender.getCustomer().getMpin())))
        {
            throw new IllegalArgumentException("Pin Anda Salah");
        } 
        if (bankAccountsSender.getAmount()-transactionRequestDto.getNominal() <0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }
        Random random = new Random();
        long randomLong = random.nextLong();
        Transactions transaction = new Transactions();
        transaction.setIdTransaction(Math.abs(randomLong));
        transaction.setBankAccounts(bankAccountsSender);
        transaction.setToAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction.setAmountTransfer((double) transactionRequestDto.getNominal());
        
        transaction.setBanks(banks);
        transaction.setNotes(transactionRequestDto.getNote());
        transaction.setType(TransactionsType.SESAMA_BANK);
        bankAccountsSender.setAmount(bankAccountsSender.getAmount()-(double) transactionRequestDto.getNominal());
        bankAccountsReceiver.setAmount(bankAccountsReceiver.getAmount()+(double) transactionRequestDto.getNominal());
        bankAccountsRepository.save(bankAccountsReceiver);
        bankAccountsRepository.save(bankAccountsSender);
        Transactions transactionsaved = transactionRepository.save(transaction);

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        // Convert Date to String
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm 'WIB'");
        String dateString = dateFormat.format(transactionsaved.getCreatedDate());
        transactionResponseDto.setTransaction_num(randomLong);
        transactionResponseDto.setTransaction_date(dateString);
        transactionResponseDto.setName_sender(bankAccountsSender.getCustomer().getName());
        transactionResponseDto.setAccountnum_sender(bankAccountsSender.getAccountNumber());
        transactionResponseDto.setName_recipient(bankAccountsReceiver.getCustomer().getName());
        transactionResponseDto.setAccountnum_recipient(bankAccountsReceiver.getAccountNumber());
        transactionResponseDto.setNominal(formatCurrency(transactionRequestDto.getNominal()));
        transactionResponseDto.setNote(transaction.getNotes());

        return transactionResponseDto;
    }
    public static String formatCurrency(int amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(amount);
    }



    
    @Transactional
    @Override
    public TransactionCheckAccountResponseDto check(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto){
        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber( transactionCheckAccountRequestDto.getAccountnum_recipient());
        if (!optionalBankAccountsReceiver.isPresent()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        // Optional<Banks> optionalBanks = bankRepository.findById(12L);
        Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        if (!optionalBanks.isPresent()){
            throw new IllegalArgumentException("Bank Tidak Ditemukan");
        }
        Banks banks = optionalBanks.get();
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();


        TransactionCheckAccountResponseDto transactionCheckAccountResponseDisplay = new TransactionCheckAccountResponseDto();
        transactionCheckAccountResponseDisplay.setAccountnum_recipient(transactionCheckAccountRequestDto.getAccountnum_recipient());
        transactionCheckAccountResponseDisplay.setName_recipient(bankAccountsReceiver.getCustomer().getName());
        transactionCheckAccountResponseDisplay.setNominal(transactionCheckAccountRequestDto.getNominal());
        transactionCheckAccountResponseDisplay.setNote(transactionCheckAccountRequestDto.getNote());

        return transactionCheckAccountResponseDisplay;
    }
    
}
