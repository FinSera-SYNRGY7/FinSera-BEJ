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
    @Autowired TransactionOtherBankRepository transactionOtherBankRepository;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @Autowired BankAccountsOtherBanksRepository bankAccountsOtherBanksRepository;
    @Autowired TransactionNumberRepository transactionNumberRepository;
    @Autowired BankRepository bankRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public TransactionResponseDto placeTransactionsIntraBank(TransactionRequestDto transactionRequestDto){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(transactionRequestDto.getId_user());
        Optional<BankAccounts>  optionalBankAccountsReceiver = bankAccountsRepository.findByAccountNumber( transactionRequestDto.getAccountnum_recipient());
        if (!optionalBankAccountsReceiver.isPresent()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        // Optional<Banks> optionalBanks = bankRepository.findById(12L);
        // Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        // if (!optionalBanks.isPresent()){
        //     throw new IllegalArgumentException("Bank Tidak Ditemukan");
        // }
        // Banks banks = optionalBanks.get();
        BankAccounts bankAccountsSender = optionalBankAccountsSender.get(0);
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();
        // if (!(bankAccountsSender.getCustomer().getMpin().equals(transactionRequestDto.getPin()))){
        //     
        // }
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
        transaction_out.setNotes(transactionRequestDto.getNote());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm 'WIB'");
        String dateString = dateFormat.format(transactionsaved.getCreatedDate());
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
        // Optional<Banks> optionalBanks = bankRepository.findById(12L);
        // Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        // if (!optionalBanks.isPresent()){
        //     throw new IllegalArgumentException("Bank Tidak Ditemukan");
        // }
        // Banks banks = optionalBanks.get();
        BankAccounts bankAccountsReceiver = optionalBankAccountsReceiver.get();

        TransactionCheckAccountResponseDto transactionCheckAccountResponseDisplay = new TransactionCheckAccountResponseDto();
        transactionCheckAccountResponseDisplay.setAccountnum_recipient(transactionCheckAccountRequestDto.getAccountnum_recipient());
        transactionCheckAccountResponseDisplay.setName_recipient(bankAccountsReceiver.getCustomer().getName());
        transactionCheckAccountResponseDisplay.setNominal(transactionCheckAccountRequestDto.getNominal());
        transactionCheckAccountResponseDisplay.setNote(transactionCheckAccountRequestDto.getNote());

        return transactionCheckAccountResponseDisplay;
    }


    @Transactional
    @Override
    public TransactionOtherBankResponse placeTransactionsInterBank(TransactionOtherBankRequest transactionOtherBankRequest){
        List<BankAccounts>  optionalBankAccountsSender = bankAccountsRepository.findBankAccountsByCustomerId(transactionOtherBankRequest.getId_user());
        List<BankAccountsOtherBanks>  optionalBankAccountsReceiver = bankAccountsOtherBanksRepository.findBankAccountsByAccountNumberAndBankId( transactionOtherBankRequest.getAccountnum_recipient(), transactionOtherBankRequest.getBank_id());
        if (optionalBankAccountsReceiver.isEmpty()) {
            throw new IllegalArgumentException("Nomor Rekening Tidak Ditemukan");
        }
        Optional<Banks> optionalBanks = bankRepository.findById(Long.valueOf(transactionOtherBankRequest.getBank_id()));
        // Optional<Banks> optionalBanks = bankRepository.findByBankName("BCA");
        if (!optionalBanks.isPresent()){
            throw new IllegalArgumentException("Bank Tidak Ditemukan");
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
        TransactionOtherBanks transaction_out = new TransactionOtherBanks();
        transaction_out.setBankAccounts(bankAccountsSender);
        transaction_out.setBankAccountsOtherBanks(bankAccountsReceiver);
        transaction_out.setTransactionsNumber(transactionsNumberSaved);
        transaction_out.setFromAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_out.setToAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction_out.setAmountTransfer((double) nominal);
        transaction_out.setNotes(transactionOtherBankRequest.getNote());
        transaction_out.setTransactionInformation(TransactionInformation.UANG_KELUAR);

        // logging money in
        TransactionOtherBanks transaction_in = new TransactionOtherBanks();
        transaction_in.setBankAccounts(bankAccountsSender);
        transaction_in.setBankAccountsOtherBanks(bankAccountsReceiver);
        transaction_in.setTransactionsNumber(transactionsNumberSaved);
        transaction_in.setFromAccountNumber(bankAccountsReceiver.getAccountNumber());
        transaction_in.setToAccountNumber(bankAccountsSender.getAccountNumber());
        transaction_in.setAmountTransfer((double) nominal);
        transaction_out.setNotes(transactionOtherBankRequest.getNote());
        transaction_in.setTransactionInformation(TransactionInformation.UANG_MASUK);

        bankAccountsSender.setAmount(bankAccountsSender.getAmount()-(double) nominal);
        bankAccountsReceiver.setAmount(bankAccountsReceiver.getAmount()+(double) transactionOtherBankRequest.getNominal());
        bankAccountsOtherBanksRepository.save(bankAccountsReceiver);
        bankAccountsRepository.save(bankAccountsSender);
        TransactionOtherBanks transactionsaved = transactionOtherBankRepository.save(transaction_out);
        transactionOtherBankRepository.save(transaction_in);

        TransactionOtherBankResponse transactionResponseDto = new TransactionOtherBankResponse();
        // Convert Date to String
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm 'WIB'");
        String dateString = dateFormat.format(transactionsaved.getCreatedDate());
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
        transactionCheckAccountResponseDisplay.setNominal(transactionCheckAccountRequestDto.getNominal());
        transactionCheckAccountResponseDisplay.setAdmin_fee(formatCurrency(2500));
        transactionCheckAccountResponseDisplay.setBank_id(transactionCheckAccountRequestDto.getBank_id());
        transactionCheckAccountResponseDisplay.setBank_name(banks.getBankName());
        transactionCheckAccountResponseDisplay.setNote(transactionCheckAccountRequestDto.getNote());

        return transactionCheckAccountResponseDisplay;
    }

    @Transactional
    @Override
    public TransferVirtualAccountResponseDto transferVA(Long id, TransferVirtualAccountRequestDto transferVirtualAccountRequestDto) {
        BankAccounts senderBankAccount = bankAccountsRepository.findByCustomerId(id);
        AccountDummyData recipientVirtualAccount = accountDummyService.checkAccount(transferVirtualAccountRequestDto.getRecipientAccountNum());
        TransactionsNumber transactionsNumber = new TransactionsNumber();
        transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
        transactionNumberRepository.save(transactionsNumber);

        //sender transaction
        Transactions senderTransaction = new Transactions();
        senderTransaction.setTransactionInformation(TransactionInformation.UANG_KELUAR);
        senderTransaction.setBankAccounts(senderBankAccount);
        senderTransaction.setBanks(senderBankAccount.getBanks());
        senderTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);
        senderTransaction.setTransactionsNumber(transactionsNumber);
        senderTransaction.setFromAccountNumber(senderBankAccount.getAccountNumber());
        senderTransaction.setToAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum());
        senderTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());
        senderTransaction.setNotes(transferVirtualAccountRequestDto.getNote());
        transactionRepository.save(senderTransaction);

        //sender update amount
        senderBankAccount.setAmount(senderBankAccount.getAmount() - transferVirtualAccountRequestDto.getNominal());
        bankAccountsRepository.save(senderBankAccount);

        //recipient transaction
        Transactions recipientTransaction = new Transactions();
        recipientTransaction.setTransactionInformation(TransactionInformation.UANG_MASUK);
        recipientTransaction.setType(TransactionsType.VIRTUAL_ACCOUNT);

        //TBD should be same with noTransaction of sender or not
        recipientTransaction.setTransactionsNumber(transactionsNumber);
        recipientTransaction.setBankAccounts(senderBankAccount);
        recipientTransaction.setBanks(senderBankAccount.getBanks());
        recipientTransaction.setFromAccountNumber(senderBankAccount.getAccountNumber());
        recipientTransaction.setToAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum());
        recipientTransaction.setAmountTransfer(transferVirtualAccountRequestDto.getNominal());
        recipientTransaction.setNotes(transferVirtualAccountRequestDto.getNote());
        transactionRepository.save(recipientTransaction);

        //recipient update amount
        recipientVirtualAccount.setAmount(
                recipientVirtualAccount.getAmount() + transferVirtualAccountRequestDto.getNominal()
        );
        accountDummyRepository.save(recipientVirtualAccount);

        TransferVirtualAccountResponseDto response = new TransferVirtualAccountResponseDto();
        response.setTransactionNum(1L);
        response.setType(TransactionsType.VIRTUAL_ACCOUNT);
        response.setTransactionDate(Date.from(Instant.now()).toString());
        response.setSenderName(senderBankAccount.getCustomer().getName());
        response.setSenderAccountNum(senderBankAccount.getAccountNumber());
        response.setRecipientName(recipientVirtualAccount.getAccountName());
        response.setRecipientAccountNum(transferVirtualAccountRequestDto.getRecipientAccountNum());
        response.setNominal(transferVirtualAccountRequestDto.getNominal().toString());
        response.setNote(transferVirtualAccountRequestDto.getNote());
        return response;
    }

}
