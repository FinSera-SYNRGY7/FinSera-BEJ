package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.transaction.*;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.util.InsufficientBalanceException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    @Autowired TransactionRepository transactionRepository;
    @Autowired
    TransactionOtherBankRepository transactionOtherBankRepository;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @Autowired
    BankAccountsOtherBanksRepository bankAccountsOtherBanksRepository;
    @Autowired TransactionNumberRepository transactionNumberRepository;
    @Autowired BankRepository bankRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired
    VirtualAccountService virtualAccountService;
    @Autowired
    VirtualAccountRepository virtualAccountRepository;
    @Autowired
    DateFormatterIndonesia dateFormatterIndonesia;

    @Transactional
    @Override
    public TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto, long idCustomers ){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(idCustomers);
        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber( transactionRequestDto.getAccountnum_recipient());
        if (!optionalBankAccountsReceiver.isPresent()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        if(transactionRequestDto.getAccountnum_recipient().equals(optionalBankAccountsSender.get(0).getAccountNumber())) {
            throw new IllegalArgumentException("Nomor rekening tujuan tidak boleh sama dengan nomor rekening pengirim");
        }

        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();

        System.out.println(transactionRequestDto.getPin());
        System.out.println(bankAccountsSender.getMpinAccount());

        if (!(passwordEncoder.matches(transactionRequestDto.getPin(), bankAccountsSender.getCustomer().getMpinAuth())))
        {
            throw new IllegalArgumentException("Pin Anda Salah");
        } 
        if (bankAccountsSender.getAmount()-transactionRequestDto.getNominal() <0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }

        // transactionNumber
        Random random = new Random();
        long randomLong = Math.abs(random.nextLong());
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(String.valueOf(randomLong));
        TransactionsNumber transactionsNumberSaved =  transactionNumberRepository.save(transactionsNumber);

        // logging money out
        Transactions transaction_out = new Transactions();
        transaction_out.setBankAccounts(bankAccountsSender);
        transaction_out.setTransactionsNumber(transactionsNumberSaved);
        transaction_out.setFromAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_out.setToAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction_out.setAmountTransfer((double) transactionRequestDto.getNominal());
        transaction_out.setNotes(transactionRequestDto.getNote());
        transaction_out.setType(TransactionsType.SESAMA_BANK);
        transaction_out.setTransactionInformation(TransactionInformation.UANG_KELUAR);

        // logging money in
        Transactions transaction_in = new Transactions();
        transaction_in.setBankAccounts(bankAccountsReceiver);
        transaction_in.setTransactionsNumber(transactionsNumberSaved);
        transaction_in.setFromAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction_in.setToAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_in.setAmountTransfer((double) transactionRequestDto.getNominal());
        transaction_in.setNotes(transactionRequestDto.getNote());
        transaction_in.setType(TransactionsType.SESAMA_BANK);
        transaction_in.setTransactionInformation(TransactionInformation.UANG_MASUK);

        bankAccountsSender.setAmount(bankAccountsSender.getAmount()-(double) transactionRequestDto.getNominal());
        bankAccountsReceiver.setAmount(bankAccountsReceiver.getAmount()+(double) transactionRequestDto.getNominal());
        bankAccountsRepository.save(bankAccountsReceiver);
        bankAccountsRepository.save(bankAccountsSender);
        Transactions transactionsaved = transactionRepository.save(transaction_out);
        transactionRepository.save(transaction_in);

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        // Convert Date to String
        String dateString = dateFormatterIndonesia.dateFormatterIND(transactionsaved.getCreatedDate());
        transactionResponseDto.setTransaction_num(String.valueOf(randomLong));
        transactionResponseDto.setTransaction_date(dateString);
        transactionResponseDto.setName_sender(bankAccountsSender.getCustomer().getName());
        transactionResponseDto.setAccountnum_sender(bankAccountsSender.getAccountNumber());
        transactionResponseDto.setName_recipient(bankAccountsReceiver.getCustomer().getName());
        transactionResponseDto.setAccountnum_recipient(bankAccountsReceiver.getAccountNumber());
        transactionResponseDto.setNominal(formatCurrency(transactionRequestDto.getNominal()));
        transactionResponseDto.setNote(transactionsaved.getNotes());

        return transactionResponseDto;
    }
    public static String formatCurrency(int amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(amount);
    }

    @Transactional
    @Override
    public TransactionCheckAccountResponseDto checkAccountIntraBank(TransactionCheckAccountRequestDto transactionCheckAccountRequestDto){

        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber( transactionCheckAccountRequestDto.getAccountnum_recipient());
        if (!optionalBankAccountsReceiver.isPresent()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();

        TransactionCheckAccountResponseDto transactionCheckAccountResponseDisplay = new TransactionCheckAccountResponseDto();
        transactionCheckAccountResponseDisplay.setAccountnum_recipient(transactionCheckAccountRequestDto.getAccountnum_recipient());
        transactionCheckAccountResponseDisplay.setName_recipient(bankAccountsReceiver.getCustomer().getName());
        // transactionCheckAccountResponseDisplay.setNominal(transactionCheckAccountRequestDto.getNominal());
        // transactionCheckAccountResponseDisplay.setNote(transactionCheckAccountRequestDto.getNote());

        return transactionCheckAccountResponseDisplay;
    }


    @Transactional
    @Override
    public TransactionOtherBankResponse placeTransactionsInterBank(TransactionOtherBankRequest transactionOtherBankRequest, long customerId){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(customerId);
        List<BankAccountsOtherBanks>  optionalBankAccountsReceiver = bankAccountsOtherBanksRepository.findBankAccountsByAccountNumberAndBankId( transactionOtherBankRequest.getAccountnum_recipient(), transactionOtherBankRequest.getBank_id());
        if (optionalBankAccountsReceiver.isEmpty()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        Optional<Banks> optionalBanks = bankRepository.findById(Long.valueOf(transactionOtherBankRequest.getBank_id()));
        // Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        if (!optionalBanks.isPresent()){
            throw new IllegalArgumentException("Bank Tidak Ditemukan");
        }

        if(transactionOtherBankRequest.getAccountnum_recipient().equals(optionalBankAccountsSender.get(0).getAccountNumber())) {
            throw new IllegalArgumentException("Nomor rekening tujuan tidak boleh sama dengan nomor rekening pengirim");
        }
        Banks banks = optionalBanks.get();
        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);
        BankAccountsOtherBanks bankAccountsReceiver = optionalBankAccountsReceiver.get(0);
        // if (!(bankAccountsSender.getCustomer().getMpin().equals(transactionRequestDto.getPin()))){
        //
        // }

        if (!(passwordEncoder.matches(transactionOtherBankRequest.getPin(), bankAccountsSender.getMpinAccount())))
        {
            throw new IllegalArgumentException("Pin Anda Salah");
        }
        int nominal = transactionOtherBankRequest.getNominal()+2500;
        if (bankAccountsSender.getAmount()-nominal <0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }

        // transactionNumber
        Random random = new Random();
        long randomLong = Math.abs(random.nextLong());
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(String.valueOf(randomLong));
        TransactionsNumber transactionsNumberSaved =  transactionNumberRepository.save(transactionsNumber);

        // logging money out
        Transactions transaction_out = new Transactions();
        transaction_out.setBankAccounts(bankAccountsSender);
        transaction_out.setTransactionsNumber(transactionsNumberSaved);
        transaction_out.setFromAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_out.setToAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction_out.setAmountTransfer((double) nominal);
        transaction_out.setNotes(transactionOtherBankRequest.getNote());
        transaction_out.setType(TransactionsType.ANTAR_BANK);
        transaction_out.setTransactionInformation(TransactionInformation.UANG_KELUAR);

//        // logging money in
//        TransactionOtherBanks transaction_in = new TransactionOtherBanks();
//        transaction_in.setBankAccounts(bankAccountsSender);
//        transaction_in.setBankAccountsOtherBanks(bankAccountsReceiver);
//        transaction_in.setTransactionsNumber(transactionsNumberSaved);
//        transaction_in.setFromAccountNumber(bankAccountsReceiver.getAccountNumber());
//        transaction_in.setToAccountNumber(bankAccountsSender.getAccountNumber());
//        transaction_in.setAmountTransfer((double) nominal);
//        transaction_in.setNotes(transactionOtherBankRequest.getNote());
//        transaction_in.setTransactionInformation(TransactionInformation.UANG_MASUK);

        bankAccountsSender.setAmount(bankAccountsSender.getAmount()-(double) nominal);
        bankAccountsReceiver.setAmount(bankAccountsReceiver.getAmount()+(double) transactionOtherBankRequest.getNominal());
        bankAccountsOtherBanksRepository.save(bankAccountsReceiver);
        bankAccountsRepository.save(bankAccountsSender);
        Transactions transactionsaved = transactionRepository.save(transaction_out);
//        transactionOtherBankRepository.save(transaction_in);

        TransactionOtherBankResponse transactionResponseDto = new TransactionOtherBankResponse();
        // Convert Date to String
        String dateString = dateFormatterIndonesia.dateFormatterIND(transactionsaved.getCreatedDate());
        transactionResponseDto.setTransaction_num(String.valueOf(randomLong));
        transactionResponseDto.setTransaction_date(dateString);
        transactionResponseDto.setName_sender(bankAccountsSender.getCustomer().getName());
        transactionResponseDto.setAccountnum_sender(bankAccountsSender.getAccountNumber());
        transactionResponseDto.setName_recipient(bankAccountsReceiver.getName());
        transactionResponseDto.setAccountnum_recipient(bankAccountsReceiver.getAccountNumber());
        transactionResponseDto.setNominal(formatCurrency(transactionOtherBankRequest.getNominal()));
        transactionResponseDto.setBank_name(banks.getBankName());
        transactionResponseDto.setAdmin_fee(formatCurrency(2500));
        transactionResponseDto.setNote(transactionsaved.getNotes());

        return transactionResponseDto;
    }

    @Transactional
    @Override
    public TransactionCheckOtherBankResponse checkAccountOtherBank(TransactionCheckOtherBankAccountRequest transactionCheckAccountRequestDto){
        List<BankAccountsOtherBanks>  optionalBankAccountsReceiver = bankAccountsOtherBanksRepository.findBankAccountsByAccountNumberAndBankId( transactionCheckAccountRequestDto.getAccountnum_recipient(), transactionCheckAccountRequestDto.getBank_id());
        if (optionalBankAccountsReceiver.isEmpty()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        Optional<Banks> optionalBanks = bankRepository.findById(Long.valueOf(transactionCheckAccountRequestDto.getBank_id()));
        // Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        if (!optionalBanks.isPresent()){
            throw new IllegalArgumentException("Bank Tidak Ditemukan");
        }
        Banks banks = optionalBanks.get();
        BankAccountsOtherBanks bankAccountsReceiver = optionalBankAccountsReceiver.get(0);

        TransactionCheckOtherBankResponse transactionCheckAccountResponseDisplay = new TransactionCheckOtherBankResponse();
        transactionCheckAccountResponseDisplay.setAccountnum_recipient(transactionCheckAccountRequestDto.getAccountnum_recipient());
        transactionCheckAccountResponseDisplay.setName_recipient(bankAccountsReceiver.getName());
        // transactionCheckAccountResponseDisplay.setNominal(transactionCheckAccountRequestDto.getNominal());
        // transactionCheckAccountResponseDisplay.setAdmin_fee(formatCurrency(2500));
        transactionCheckAccountResponseDisplay.setBank_id(transactionCheckAccountRequestDto.getBank_id());
        transactionCheckAccountResponseDisplay.setBank_name(banks.getBankName());
        // transactionCheckAccountResponseDisplay.setNote(transactionCheckAccountRequestDto.getNote());

        return transactionCheckAccountResponseDisplay;
    }

    @Override
    @Transactional
    public List<?> historyTransaction(long idCustomers){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(idCustomers);
        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);

        List<Transactions> optionalHistory =  transactionRepository.findDistinctByToAccountNumber(bankAccountsSender.getAccountNumber(), TransactionsType.SESAMA_BANK);
        if (optionalHistory.isEmpty()) {
            throw new IllegalArgumentException("Transaksi belum ada");
        }
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        for (Transactions transaction : optionalHistory) {
            Map<String, Object> data = new HashMap<>();
            data.put("name_recipient", transaction.getBankAccounts().getCustomer().getName());
            data.put("bank_name", "Bank BCA");
            data.put("account_number_recipient", transaction.getBankAccounts().getAccountNumber());
            historyList.add(data);
        }
        
        return historyList;
    }

    @Override
    @Transactional
    public List<?> historyTransactionInterBank(long idCustomers){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(idCustomers);
        log.info("bank account: " + optionalBankAccountsSender.get(0).getAccountNumber());
//        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);
        Optional<List<Transactions>> sender = transactionRepository.getAllHistoryByToAccountNumber(optionalBankAccountsSender.get(0).getAccountNumber(), TransactionsType.ANTAR_BANK);
//        List<Transactions> optionalHistory =  transactionRepository.findDistinctByToAccountNumber("789012345", TransactionsType.ANTAR_BANK);
//        log.info("optional History: " + optionalHistory.size());
        if (sender.isEmpty()) {
            throw new IllegalArgumentException("Transaksi belum ada");
        }
//        log.info("optional History: " + optionalHistory);
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        for (Transactions transaction : sender.get()) {
            Optional<BankAccountsOtherBanks> bankAccountsOtherBanks = bankAccountsOtherBanksRepository.findByAccountNumber(transaction.getToAccountNumber());
            Map<String, Object> data = new HashMap<>();
            data.put("name_recipient", bankAccountsOtherBanks.get().getName());
            data.put("bank_name", bankAccountsOtherBanks.get().getBanks().getBankName());
            data.put("account_number_recipient", bankAccountsOtherBanks.get().getAccountNumber());
            historyList.add(data);
        }
        
        return historyList;
    }

    @Transactional
    @Override
    public TransferVirtualAccountResponseDto transferVA(Long id, TransferVirtualAccountRequestDto transferVirtualAccountRequestDto) {
        BankAccounts senderBankAccount = bankAccountsRepository.findByCustomerId(id);
        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum());
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();
        VirtualAccounts recipientVirtualAccount = virtualAccountService.checkAccount(transferVirtualAccountRequestDto.getRecipientAccountNum());
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        transactionNumberRepository.save(transactionsNumber);
        Double adminFee = 2500.0;

        //sender transaction
        Transactions senderTransaction = new Transactions();
        senderTransaction.setTransactionInformation(TransactionInformation.UANG_KELUAR);
        senderTransaction.setBankAccounts(senderBankAccount);
        senderTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);
        senderTransaction.setTransactionsNumber(transactionsNumber);
        senderTransaction.setFromAccountNumber(senderBankAccount.getAccountNumber());
        senderTransaction.setToAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum());
        senderTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());
        senderTransaction.setNotes(transferVirtualAccountRequestDto.getNote());
        transactionRepository.save(senderTransaction);

        //sender update amount
        senderBankAccount.setAmount(senderBankAccount.getAmount() - transferVirtualAccountRequestDto.getNominal() - adminFee);
        bankAccountsRepository.save(senderBankAccount);

        //recipient transaction
        Transactions recipientTransaction = new Transactions();
        recipientTransaction.setTransactionInformation(TransactionInformation.UANG_MASUK);
        recipientTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);

        //TBD should be same with noTransaction of sender or not
        recipientTransaction.setTransactionsNumber(transactionsNumber);
        recipientTransaction.setBankAccounts(bankAccountsReceiver);
        recipientTransaction.setFromAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum());
        recipientTransaction.setToAccountNumber(senderBankAccount.getAccountNumber());
        recipientTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());

        recipientTransaction.setNotes(transferVirtualAccountRequestDto.getNote());
        transactionRepository.save(recipientTransaction);

        //recipient update amount and save
        recipientVirtualAccount.setAmount(
                recipientVirtualAccount.getAmount() + transferVirtualAccountRequestDto.getNominal()
        );
        recipientVirtualAccount.setSavedAccount(transferVirtualAccountRequestDto.getSaveAccount());
        virtualAccountRepository.save(recipientVirtualAccount);

        TransferVirtualAccountResponseDto response = new TransferVirtualAccountResponseDto();
        response.setTransactionNum(transactionsNumber.getTransactionNumber());
        response.setType(TransactionsType.VIRTUAL_ACCOUNT);
        response.setTransactionDate(Date.from(Instant.now()).toString());
        response.setSenderName(senderBankAccount.getCustomer().getName());
        response.setSenderAccountNum(senderBankAccount.getAccountNumber());
        response.setRecipientName(recipientVirtualAccount.getAccountName());
        response.setRecipientAccountNum(transferVirtualAccountRequestDto.getRecipientAccountNum());
        response.setNominal(transferVirtualAccountRequestDto.getNominal().toString());
        response.setAdminFee(String.valueOf(adminFee));
        response.setNote(transferVirtualAccountRequestDto.getNote());
        return response;
    }

}
