package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.model.entity.BankAccounts;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.model.enums.Gender;
import com.finalproject.finsera.finsera.model.enums.StatusUser;
import com.finalproject.finsera.finsera.repository.CustomersRepository;
import com.finalproject.finsera.finsera.repository.InfoSaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.Instant;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(CustomersRepository customersRepository, InfoSaldoRepository bankAccountsRepository) {
        return args -> {

            if (customersRepository.count() == 0) {

                Customers customer1 = new Customers();
                customer1.setName("John Doe");
                customer1.setNik("1234567890123456");
                customer1.setAddress("123 Main Street");
                customer1.setGender(Gender.LAKI_LAKI);
                customer1.setFatherName("Richard Doe");
                customer1.setMotherName("Jane Doe");
                customer1.setPhoneNumber("081234567890");
                customer1.setIncome(5000.0);
                customer1.setUsername("johndoe");
                customer1.setPassword("password123");
                customer1.setEmail("john.doe@example.com");
                customer1.setMpin("1234");
                customer1.setStatusUser(StatusUser.ACTIVE);
                customer1.setCreatedAt(Timestamp.from(Instant.now()));

                Customers customer2 = new Customers();
                customer2.setName("Jane Smith");
                customer2.setNik("2345678901234567");
                customer2.setAddress("456 Elm Street");
                customer2.setGender(Gender.PEREMPUAN);
                customer2.setFatherName("John Smith");
                customer2.setMotherName("Emily Smith");
                customer2.setPhoneNumber("082345678901");
                customer2.setIncome(6000.0);
                customer2.setUsername("janesmith");
                customer2.setPassword("password456");
                customer2.setEmail("jane.smith@example.com");
                customer2.setMpin("5678");
                customer2.setStatusUser(StatusUser.ACTIVE);
                customer2.setCreatedAt(Timestamp.from(Instant.now()));


                customersRepository.save(customer1);
                customersRepository.save(customer2);



                BankAccounts bankAccount1 = new BankAccounts();
                bankAccount1.setCustomer(customer1);
                bankAccount1.setAccountNumber("1234567890");
                bankAccount1.setAmount(10000.0);
                bankAccount1.setDeletedAt(null);

                BankAccounts bankAccount2 = new BankAccounts();
                bankAccount2.setCustomer(customer2);
                bankAccount2.setAccountNumber("0987654321");
                bankAccount2.setAmount(15000.0);
                bankAccount2.setDeletedAt(null);


                bankAccountsRepository.save(bankAccount1);
                bankAccountsRepository.save(bankAccount2);
            }
        };
    }
}
