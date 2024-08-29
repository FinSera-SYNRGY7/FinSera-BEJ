package com.finalproject.finsera.finsera;

import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.BankAccountsRepository;
import com.finalproject.finsera.finsera.repository.TransactionRepository;
import com.finalproject.finsera.finsera.repository.VirtualAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class VirtualAccountTest {
    @Mock
    VirtualAccountRepository virtualAccountRepository;

    @Mock
    BankAccountsRepository bankAccountsRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Customers customers = new Customers();
        customers.setUsername("test");
        customers.setPassword("test");

    }

    @Test
    void transferVirtualAccount(){

    }
}
