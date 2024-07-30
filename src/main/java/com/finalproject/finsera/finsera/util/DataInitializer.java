//package com.finalproject.finsera.finsera.util;
//
//import com.finalproject.finsera.finsera.model.entity.BankAccounts;
//import com.finalproject.finsera.finsera.model.entity.Banks;
//import com.finalproject.finsera.finsera.model.entity.Customers;
//import com.finalproject.finsera.finsera.model.entity.Transactions;
//import com.finalproject.finsera.finsera.model.enums.Gender;
//import com.finalproject.finsera.finsera.model.enums.StatusUser;
//import com.finalproject.finsera.finsera.model.enums.TransactionInformation;
//import com.finalproject.finsera.finsera.model.enums.TransactionsType;
//import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
//import com.finalproject.finsera.finsera.repository.BankRepository;
//import com.finalproject.finsera.finsera.repository.CustomerRepository;
//import com.finalproject.finsera.finsera.repository.TransactionRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.util.Arrays;
//
//import static com.finalproject.finsera.finsera.util.TransactionNumberGenerator.generateTransactionNumber;
//
//@Configuration
//@AllArgsConstructor
//public class DataInitializer {
//
//    private final CustomerRepository customersRepository;
//    private final BankRepository bankRepository;
//    private final BankAccountsRepository bankAccountsRepository;
//    private final TransactionRepository transactionRepository;
//
//    @Bean
//    public CommandLineRunner loadData() {
//        return args -> {
//
//            if (customersRepository.count() == 0) {
//
//                Customers customer1 = new Customers();
//                customer1.setName("John Doe");
//                customer1.setNik("1234567890123456");
//                customer1.setAddress("123 Main Street");
//                customer1.setGender(Gender.LAKI_LAKI);
//                customer1.setFatherName("Richard Doe");
//                customer1.setMotherName("Jane Doe");
//                customer1.setPhoneNumber("081234567890");
//                customer1.setIncome(5000.0);
//                customer1.setUsername("johndoe");
//                customer1.setPassword("$2y$10$iA4o9T3J1GMkGfLYloCpFO7sTqaM/WElIv3QyVZyyY40SaHg.Z5Se"); // password123
//                customer1.setEmail("john.doe@example.com");
//                customer1.setMpinAuth("$2a$12$tA7ILdk.dYhPM1b3vffVJOcSXVphHfHK6FL1m39u7kptss4Une4e2"); // 123456
//                customer1.setStatusUser(StatusUser.ACTIVE);
//                customer1.setCreatedDate(Timestamp.from(Instant.now()));
//
//                Customers customer2 = new Customers();
//                customer2.setName("Jane Smith");
//                customer2.setNik("2345678901234567");
//                customer2.setAddress("456 Elm Street");
//                customer2.setGender(Gender.PEREMPUAN);
//                customer2.setFatherName("John Smith");
//                customer2.setMotherName("Emily Smith");
//                customer2.setPhoneNumber("082345678901");
//                customer2.setIncome(6000.0);
//                customer2.setUsername("janesmith");
//                customer2.setPassword("$2y$10$O3uPZEA1eSUfpQAulDnLPeSlv1JjCgNVes71z3UvadsRasnH2thHi"); // password456
//                customer2.setEmail("jane.smith@example.com");
//                customer2.setMpinAuth("$2a$12$tA7ILdk.dYhPM1b3vffVJOcSXVphHfHK6FL1m39u7kptss4Une4e2"); // 123456
//                customer2.setStatusUser(StatusUser.ACTIVE);
//                customer2.setCreatedDate(Timestamp.from(Instant.now()));
//
//
//                customersRepository.save(customer1);
//                customersRepository.save(customer2);
//
//
//                BankAccounts bankAccount1 = new BankAccounts();
//                bankAccount1.setCustomer(customer1);
//                bankAccount1.setAccountNumber("123456789");
//                bankAccount1.setAmount(1000.0);
//                bankAccount1.setMpinAccount("1234");
//
//                BankAccounts bankAccount2 = new BankAccounts();
//                bankAccount2.setCustomer(customer2);
//                bankAccount2.setAccountNumber("987654321");
//                bankAccount2.setAmount(2000.0);
//                bankAccount2.setMpinAccount("5678");
//
//                bankAccountsRepository.save(bankAccount1);
//                bankAccountsRepository.save(bankAccount2);
//
//                Banks bank1 = new Banks();
//                bank1.setBankName("BCA");
//                bank1.setBankCode("014");
//
//                bankRepository.save(bank1);
//
//
//                for (int i = 0; i < 5; i++) {
//                    Transactions transaction1 = new Transactions();
//                    transaction1.setBankAccounts(bankAccount1);
//                    transaction1.setNoTransaction(generateTransactionNumber());
//                    transaction1.setFromAccountNumber(bankAccount1.getAccountNumber());
//                    transaction1.setToAccountNumber(bankAccount2.getAccountNumber());
//                    transaction1.setBanks(bank1);
//                    transaction1.setAmountTransfer(100.0 + i);
//                    transaction1.setNotes("Transaction " + (i + 1));
//                    transaction1.setType(TransactionsType.SESAMA_BANK);
//                    transaction1.setTransactionInformation(TransactionInformation.UANG_KELUAR);
//                    transaction1.setCreatedDate(Timestamp.from(Instant.now()));
//
//                    transactionRepository.save(transaction1);
//                }
//
//                for (int i = 0; i < 5; i++) {
//
//                    Transactions transaction2 = new Transactions();
//                    transaction2.setBankAccounts(bankAccount1);
//                    transaction2.setNoTransaction(generateTransactionNumber());
//                    transaction2.setFromAccountNumber(bankAccount2.getAccountNumber());
//                    transaction2.setToAccountNumber(bankAccount1.getAccountNumber());
//                    transaction2.setBanks(bank1);
//                    transaction2.setAmountTransfer(200.0 + i);
//                    transaction2.setNotes("Transaction " + (i + 1));
//                    transaction2.setType(TransactionsType.SESAMA_BANK);
//                    transaction2.setTransactionInformation(TransactionInformation.UANG_MASUK);
//                    transaction2.setCreatedDate(Timestamp.from(Instant.now()));
//
//                    transactionRepository.save(transaction2);
//                }
//
//
//            }
//        };
//    }
//}