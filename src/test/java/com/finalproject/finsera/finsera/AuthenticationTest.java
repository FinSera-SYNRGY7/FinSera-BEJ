package com.finalproject.finsera.finsera;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.controller.CustomerController;
import com.finalproject.finsera.finsera.dto.AuthRequest;
import com.finalproject.finsera.finsera.dto.BaseResponse;
import com.finalproject.finsera.finsera.dto.login.LoginRequestDto;
import com.finalproject.finsera.finsera.dto.login.LoginResponseDto;
import com.finalproject.finsera.finsera.dto.login.ReloginRequestDto;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import com.finalproject.finsera.finsera.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;


    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;


    private BaseResponse authResponse;

    @BeforeEach
    public void init() {
        authResponse = new BaseResponse();

    }

    @Test
    public void testLogin() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("customerusername");
        loginRequestDto.setPassword("securepassword");

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/auth/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );
        resultActions
                .andExpect(status().isOk())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    authResponse = objectMapper.readValue(responseBody, BaseResponse.class);
                    assertEquals("Login success", authResponse.getMessage());
                });

    }

//    @Test
//    public void testRelogin() throws Exception{
//
//        ReloginRequestDto reloginRequestDto = new ReloginRequestDto();
//        reloginRequestDto.setMpin("123456");
//
//        ResultActions resultActions = mockMvc.perform(
//                post("/auth/reogin")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN0b21lcnVzZXJuYW1lIiwidXNlcklkIjo3LCJpYXQiOjE3MjE3Mzk4NzgsImV4cCI6NjAxNzIxNzM5ODc4fQ.iot8aFV3bL9sgoYSf7ujhuGNGLKIYXUS6gHcF2FleJw")
//                        .content(objectMapper.writeValueAsString(reloginRequestDto))
//        );
//        resultActions
//                .andExpect(status().isOk())
//                .andDo(result -> {
//                    String responseBody = result.getResponse().getContentAsString();
//                    authResponse = objectMapper.readValue(responseBody, BaseResponse.class);
//                    assertEquals("Login success", authResponse);
//                });
//
//    }
}
