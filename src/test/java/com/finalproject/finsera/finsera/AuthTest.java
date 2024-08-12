package com.finalproject.finsera.finsera;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import io.swagger.v3.oas.annotations.extensions.Extension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
//@ExtendWith(MockitoExtension.class)
public class AuthTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private BaseResponse baseResponse;

    @BeforeEach
    void setUp() {
        baseResponse = new BaseResponse<>();
        customerRepository.deleteAll();
    }

    @Test
    void loginSuccess() throws Exception{
//        Customers customers = new Customers();
//        customers.setUsername("username");
//        customers.setPassword(passwordEncoder.encode("password"));
//        customerRepository.save(customers);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("username");
        loginRequestDto.setPassword("password");

        mockMvc.perform(
                post("/api/v1/auth/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        )
                .andExpect(status().isOk())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    baseResponse = objectMapper.readValue(responseBody, BaseResponse.class);
                    assertEquals("login success", baseResponse);
                });

    }

    @Test
    void loginFailed() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("test");
        loginRequestDto.setPassword("test");

        mockMvc.perform(
                post("/api/v1/auth/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), BaseResponse.class);
        });
        assertNotNull(baseResponse.getCode());

    }
}
