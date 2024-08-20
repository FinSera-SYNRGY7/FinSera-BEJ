package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.virtualAccount.AccountLastTransactionResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CheckVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.CreateVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountRequestDto;
import com.finalproject.finsera.finsera.dto.virtualAccount.transferVirtualAccount.TransferVirtualAccountResponseDto;
import com.finalproject.finsera.finsera.model.entity.*;
import com.finalproject.finsera.finsera.model.enums.AccountType;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
import com.finalproject.finsera.finsera.model.enums.TransactionsType;
import com.finalproject.finsera.finsera.repository.*;
import com.finalproject.finsera.finsera.service.VirtualAccountService;
import com.finalproject.finsera.finsera.util.DateFormatterIndonesia;
import com.finalproject.finsera.finsera.util.TransactionNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class VirtualAccountServiceImpl implements VirtualAccountService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    BankAccountsRepository bankAccountsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionNumberRepository transactionNumberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public VirtualAccounts createVirtualAccount(CreateVirtualAccountRequestDto createVirtualAccountRequestDto) {
        VirtualAccounts account = new VirtualAccounts();
        account.setAccountName(createVirtualAccountRequestDto.getAccountName());
        account.setVirtualAccountNumber(createVirtualAccountRequestDto.getAccountNumber());
        account.setAccountType(AccountType.VIRTUAL_ACCOUNT);
        account.setNominal(createVirtualAccountRequestDto.getNominal());
        virtualAccountRepository.save(account);
        return account;
    }

    @Override
    public BankAccounts checkBankAccount(Long id) {
        return bankAccountsRepository.findById(id).get();
    }

    @Override
    public List<Transactions> getAll() {
        List<Transactions> transactionsList = transactionRepository.findAll();
        return transactionsList;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getLastTransactionAccountVA() {
        List<Transactions> transactionsList = transactionRepository.getLastAccountTransactionVA();

        if (transactionsList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaksi tidak ditemukan");
        } else {
            Set<String> seenVirtualAccountNumbers = new HashSet<>();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "sukses");
            response.put("data", transactionsList.stream()
                    .filter(transactions -> seenVirtualAccountNumbers.add(transactions.getToAccountNumber()))
                    .map(transactions -> new AccountLastTransactionResponseDto(
                      virtualAccountRepository.findByVirtualAccountNumber(transactions.getToAccountNumber()).get().getAccountName(),
                            transactions.getToAccountNumber()
                    )));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<Map<String, Object>> checkVirtualAccount(CheckVirtualAccountRequestDto checkVirtualAccountRequestDto) {
        VirtualAccounts virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(checkVirtualAccountRequestDto.getVirtualAccountNumber())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtual Account tidak ditemukan"));


        CheckVirtualAccountResponseDto responseVirtualAccount = new CheckVirtualAccountResponseDto();
        responseVirtualAccount.setAccountNum(virtualAccounts.getVirtualAccountNumber());
        responseVirtualAccount.setAccountName(virtualAccounts.getAccountName());
        responseVirtualAccount.setNominal(virtualAccounts.getNominal());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "sukses");
        response.put("data", responseVirtualAccount);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> transferVA(Long id,
                                                        TransferVirtualAccountRequestDto transferVirtualAccountRequestDto) {
        Customers customers = customerRepository.findById(id).get();
        if (customers.getStatusUser().equals(StatusUser.INACTIVE)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun anda tidak aktif");
        }

        BankAccounts senderBankAccount = bankAccountsRepository.findByCustomerId(id);
        VirtualAccounts virtualAccounts = virtualAccountRepository.findByVirtualAccountNumber(
                transferVirtualAccountRequestDto.getVirtualAccountNumber()
        ).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtual Account tidak ditemukan"));

        if(virtualAccounts.getVirtualAccountNumber() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomor Virtual Account tidak ditemukan");
        }
        if (!passwordEncoder.matches(
                transferVirtualAccountRequestDto.getMpinAccount(),
                senderBankAccount.getMpinAccount()
        )) {
            int newFailAttempts = senderBankAccount.getFailedAttempt() + 1;
            senderBankAccount.setFailedAttempt(newFailAttempts);
            bankAccountsRepository.save(senderBankAccount);
            if (senderBankAccount.getFailedAttempt() > 3){
                customers.setStatusUser(StatusUser.INACTIVE);
                customers.setBannedTime(Date.from(Instant.now()));
                customerRepository.save(customers);

                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun anda terblokir");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pin yang anda masukkan salah");
        } else if (virtualAccounts.getNominal() > senderBankAccount.getAmount()) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Saldo Anda Tidak Cukup");
        } else {
            senderBankAccount.setFailedAttempt(0);
            bankAccountsRepository.save(senderBankAccount);

            customers.setBannedTime(null);
            customerRepository.save(customers);

            TransactionsNumber transactionsNumber = new TransactionsNumber();
            transactionsNumber.setTransactionNumber(TransactionNumberGenerator.generateTransactionNumber());
            transactionNumberRepository.save(transactionsNumber);

            //sender transaction
            Transactions transferVirtualAccount = new Transactions();
            transferVirtualAccount.setTransactionInformation(TransactionInformation.UANG_KELUAR);
            transferVirtualAccount.setBankAccounts(senderBankAccount);
            transferVirtualAccount.setType(TransactionsType.VIRTUAL_ACCOUNT);
            transferVirtualAccount.setTransactionsNumber(transactionsNumber);
            transferVirtualAccount.setFromAccountNumber(senderBankAccount.getAccountNumber());
            transferVirtualAccount.setToAccountNumber(virtualAccounts.getVirtualAccountNumber());
            transferVirtualAccount.setAmountTransfer(virtualAccounts.getNominal());
            transactionRepository.save(transferVirtualAccount);

            //update sender amount
            senderBankAccount.setAmount(senderBankAccount.getAmount() - virtualAccounts.getNominal());
            bankAccountsRepository.save(senderBankAccount);

            TransferVirtualAccountResponseDto transferVAResponse = new TransferVirtualAccountResponseDto();
            transferVAResponse.setTransactionDate(DateFormatterIndonesia.dateFormatterIND(Date.from(Instant.now())));
            transferVAResponse.setTransactionNum(transactionsNumber.getTransactionNumber());
            transferVAResponse.setRecipientName(virtualAccounts.getAccountName());
            transferVAResponse.setRecipientVirtualAccountNum(virtualAccounts.getVirtualAccountNumber());
            transferVAResponse.setType(TransactionsType.VIRTUAL_ACCOUNT);
            transferVAResponse.setNominal(transferVirtualAccount.getAmountTransfer().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "sukses");
            response.put("data", transferVAResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
