//package com.finalproject.finsera.finsera.util;
//
//import com.finalproject.finsera.finsera.model.entity.BankAccounts;
//import com.finalproject.finsera.finsera.model.entity.Customers;
//import com.finalproject.finsera.finsera.model.enums.Gender;
//import com.finalproject.finsera.finsera.model.enums.StatusUser;
//import com.finalproject.finsera.finsera.repository.CustomerRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    public CommandLineRunner loadData(CustomerRepository customersRepository) {
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
//                customer1.setMpin("1234");
//                customer1.setStatusUser(StatusUser.ACTIVE);
//                customer1.setCreatedAt(Timestamp.from(Instant.now()));
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
//                customer2.setMpin("5678");
//                customer2.setStatusUser(StatusUser.ACTIVE);
//                customer2.setCreatedAt(Timestamp.from(Instant.now()));
//
//
//                customersRepository.save(customer1);
//                customersRepository.save(customer2);
//
//            }
//        };
//    }
//}