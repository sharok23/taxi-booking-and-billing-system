package com.edstem.taxibookingandbillingsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edstem.taxibookingandbillingsystem.contract.request.AccountBalanceRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.LoginRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.AccountBalanceResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.LoginResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.SignupResponse;
import com.edstem.taxibookingandbillingsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private UserService userService;

    @Test
    void testSignUp() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("sharok", "sharok23@gmail.com", "Helloworld");
        SignupResponse expectedResponse = new SignupResponse(1L, "sharok");
        when(userService.userSignup(any(SignupRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("sharok23@gmail.com", "Helloworld");
        LoginResponse expectedResponse =
                new LoginResponse(
                        "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoic2hhcm9rIiwiaWQiOjIsInN1YiI6IjJAZ21haWwuY29tIiwiaWF0IjoxNzA2OTM5Njg3LCJleHAiOjE3MDcwMjYwODd9.FhuhQzgMlXpdCyEJ0hfm8VNbvBYgv6eeZcwhpacfQEg");
        when(userService.userLogin(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testUpdateAccountBalance() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest(500.0);
        AccountBalanceResponse expectedResponse = new AccountBalanceResponse(1500.0);
        when(userService.updateAccountBalance(any(AccountBalanceRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}
