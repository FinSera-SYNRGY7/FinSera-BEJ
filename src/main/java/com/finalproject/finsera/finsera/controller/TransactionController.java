package com.finalproject.finsera.finsera.controller;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.finsera.finsera.dto.TransactionCheckAccountRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionCheckAccountResponseDto;
import com.finalproject.finsera.finsera.dto.TransactionRequestDto;
import com.finalproject.finsera.finsera.dto.TransactionResponseDto;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.service.TransactionService;
import com.finalproject.finsera.finsera.service.impl.TransactionServiceImpl;

@Component
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    @Autowired TransactionServiceImpl transactionServiceImpl;
    @Autowired BankAccountsRepository bankAccountsRepository;
    @PostMapping("/transaction-intra/create")
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AccountDummyService accountDummyService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountDummyRepository accountDummyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody TransactionRequestDto transactionRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionResponseDto transactionResponseDto =  transactionServiceImpl.placeTransactionsIntraBank(transactionRequestDto);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Transaksi Berhasil");
            response.put("status", 200);
            response.put("data", transactionResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            if (e instanceof ConnectException){
                response.put("status", 503);
                response.put("message", "Connection lost with the server. Please try again later.");
                return ResponseEntity.status(503).body(response);
            }
            else{
                response.put("status", 402);
                response.put("message", e.getMessage());
                return ResponseEntity.status(402).body(response);
            }

        }
    }

    @PostMapping("/transaction-intra/check")
    public ResponseEntity<Map<String, Object>> checkTransaction(@RequestBody TransactionCheckAccountRequestDto transactionCheckAccountRequestDto) {
        System.out.println(transactionCheckAccountRequestDto.getAccountnum_recipient());
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionCheckAccountResponseDto transactionCheckAccountResponseDto =  transactionServiceImpl.checkAccountIntraBank(transactionCheckAccountRequestDto);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Data Rekening tersedia");
            response.put("status", 200);
            response.put("data", transactionCheckAccountResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

    @PostMapping(value ={"/transaction-inter/create"})
    public ResponseEntity<Map<String, Object>> createTransactionInter(@RequestBody TransactionOtherBankRequest transactionOtherBankRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionOtherBankResponse transactionResponseDto =  transactionServiceImpl.placeTransactionsInterBank(transactionOtherBankRequest);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Transaksi Berhasil");
            response.put("status", 200);
            response.put("data", transactionResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            if (e instanceof ConnectException){
                response.put("status", 503);
                response.put("message", "Connection lost with the server. Please try again later.");
                return ResponseEntity.status(503).body(response);
            }
            else{
                response.put("status", 402);
                response.put("message", e.getMessage());
                return ResponseEntity.status(402).body(response);
            }

        }
    }
    @PostMapping(value ={"/transaction-inter/check"})
    public ResponseEntity<Map<String, Object>> checkTransactionInter(@RequestBody TransactionCheckOtherBankAccountRequest transactionCheckOtherBankAccountRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            TransactionCheckOtherBankResponse transactionCheckAccountResponseDto =  transactionServiceImpl.checkAccountOtherBank(transactionCheckOtherBankAccountRequest);
            // Map<String, Object> data = new HashMap<>();
            // data.put("data", transactionResponseDto);
            response.put("message", "Data Rekening tersedia");
            response.put("status", 200);
            response.put("data", transactionCheckAccountResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 402);
            response.put("message", e.getMessage());
            return ResponseEntity.status(402).body(response);
        }
    }

    @PostMapping("/virtual-account")
    public ResponseEntity<Map<String, Object>> virtualAccount(@RequestHeader("Authorization") String token, @RequestBody TransferVirtualAccountRequestDto transferVirtualAccountRequestDto){
        String jwt = token.substring("Bearer ".length());
        Long userId = jwtUtil.getId(jwt);
        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(userId);
        Optional<AccountDummyData> accountDummyData = Optional.ofNullable(accountDummyRepository.findByAccountNumber(transferVirtualAccountRequestDto.getRecipientAccountNum()));

        if (!accountDummyData.isPresent()){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "recipient account not found");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (bankAccounts.getAmount() < transferVirtualAccountRequestDto.getNominal()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "your balance is insufficient");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (!bankAccounts.getMpinAccount().equals(transferVirtualAccountRequestDto.getMpinAccount())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "your pin is invalid");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            TransferVirtualAccountResponseDto transferVirtualAccount = transactionServiceImpl.transferVA(userId, transferVirtualAccountRequestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", transferVirtualAccount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

//    @PostMapping("/hello")
//    public String hello(@RequestHeader("Authorization") String token, @RequestBody TransferVirtualAccountRequestDto transferVirtualAccountRequestDto){
////        Long id = 1L;
////        String accountNum = "1234567890";
//
//        String jwt = token.substring("Bearer ".length());
//        Long id = jwtUtil.getId(jwt);
//        System.out.println(id);
//
//        BankAccounts bankAccounts = bankAccountsRepository.findByCustomerId(id);
//        System.out.println("customer id  = " + bankAccounts.getCustomer().getIdCustomers());
//        System.out.println("customer name = " + bankAccounts.getAccountNumber());
//
//        AccountDummyData recipientVirtualAccount = accountDummyService.checkAccount(
//                transferVirtualAccountRequestDto.getRecipientAccountNum()
//        );
//        System.out.println("to : " + recipientVirtualAccount.getAccountName());
//        return "from : " + bankAccounts.getCustomer().getUsername() + "\n" + "to : " + recipientVirtualAccount.getAccountName();
//    }

    @PostMapping("/get-va")
    public String getVA(@RequestParam String accountNum){
        AccountDummyData recipient = accountDummyService.checkAccount(accountNum);
        System.out.println(recipient.getAccountName());

        return "account name " + recipient.getAccountName();
    }
}
