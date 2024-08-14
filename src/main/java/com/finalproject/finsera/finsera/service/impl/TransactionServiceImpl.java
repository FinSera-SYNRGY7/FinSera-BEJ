package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.transaction.*;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.util.InsufficientBalanceException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.text.NumberFormat;

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
    DateFormatterIndonesia dateFormatterIndonesia;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    @Override
    public TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto, long idCustomers ){
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findById(idCustomers)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
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


        if (bankAccountsSender.getAmount()-transactionRequestDto.getNominal() <0) {
            throw new InsufficientBalanceException("Saldo Anda Tidak Cukup");
        }
        if (!(passwordEncoder.matches(transactionRequestDto.getPin(), bankAccountsSender.getMpinAccount()))) {
            int newFailAttempts = bankAccountsSender.getFailedAttempt() + 1;
            bankAccountsSender.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(bankAccountsSender);
            if (bankAccountsSender.getFailedAttempt() > 3) {
                customers.get().setStatusUser(StatusUser.INACTIVE);
                customers.get().setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers.get());
                throw new IllegalArgumentException("Your account is banned");
            }
            throw new IllegalArgumentException("Pin Anda Salah");
        } else {
            bankAccountsSender.setFailedAttempt(0);
            bankAccountsRepository.save(bankAccountsSender);
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
        Optional<Customers> customers = Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
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

        if (!(passwordEncoder.matches(transactionOtherBankRequest.getPin(), bankAccountsSender.getMpinAccount()))) {
            int newFailAttempts = bankAccountsSender.getFailedAttempt() + 1;
            bankAccountsSender.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(bankAccountsSender);
            if (bankAccountsSender.getFailedAttempt() > 3) {
                customers.get().setStatusUser(StatusUser.INACTIVE);
                customers.get().setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers.get());
                throw new IllegalArgumentException("Your account is banned");
            }
            throw new IllegalArgumentException("Pin Anda Salah");
        } else {
            bankAccountsSender.setFailedAttempt(0);
            bankAccountsRepository.save(bankAccountsSender);
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
        String dateString = DateFormatterIndonesia.dateFormatterIND(transactionsaved.getCreatedDate());
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

}
