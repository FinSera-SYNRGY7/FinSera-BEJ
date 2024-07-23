package com.finalproject.finsera.finsera;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.controller.CustomerController;
import com.finalproject.finsera.finsera.dto.AuthRequest;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        Customers customer = customerRepository.findByUsername("customerusername").get();
        if(customer.equals(null)){
            customer = new Customers();
            customer.setUsername("customerusername");
            customer.setPassword("securepassword");
            customerRepository.save(customer);
        }
    }

    @Test
    public void testLogin() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("customerusername");
        loginRequestDto.setPassword("securepassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            ResponseEntity<?> responseEntity = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseEntity.class);
            assertEquals(responseEntity.getStatusCodeValue(), 200);
        });

    }
}
