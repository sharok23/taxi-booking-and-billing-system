package com.edstem.taxibookingandbillingsystem.controller;

import com.edstem.taxibookingandbillingsystem.contract.request.LoginRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testSignUp() throws Exception {
        SignupRequest signupRequest = new SignupRequest("sharok","sharok23@gmail.com","Helloworld");
        SignupResponse expectedResponse = new SignupResponse(1L,"sharok");
        when(userService.userSignup(any(SignupRequest.class))).thenReturn(expectedResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));

        verify(userService, times(1)).userSignup(any(SignupRequest.class));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("sharok23@gmail.com","Helloworld");
        LoginResponse expectedResponse = new LoginResponse("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoic2hhcm9rIiwiaWQiOjIsInN1YiI6IjJAZ21haWwuY29tIiwiaWF0IjoxNzA2OTM5Njg3LCJleHAiOjE3MDcwMjYwODd9.FhuhQzgMlXpdCyEJ0hfm8VNbvBYgv6eeZcwhpacfQEg");
        when(userService.userLogin(any(LoginRequest.class))).thenReturn(expectedResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));

        verify(userService, times(1)).userLogin(any(LoginRequest.class));
    }
}
