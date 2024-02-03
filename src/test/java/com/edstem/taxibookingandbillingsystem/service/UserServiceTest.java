package com.edstem.taxibookingandbillingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.edstem.taxibookingandbillingsystem.contract.request.AccountBalanceRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.LoginRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.AccountBalanceResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.LoginResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.SignupResponse;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import com.edstem.taxibookingandbillingsystem.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JwtService jwtService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userRepository = Mockito.mock(UserRepository.class);
        jwtService = Mockito.mock(JwtService.class);
        modelMapper = new ModelMapper();
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, modelMapper, jwtService, passwordEncoder);
    }

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest("Sharok", "sharok@gmail.com", "Helloworld");
        User user = modelMapper.map(request, User.class);
        SignupResponse expectedResponse = modelMapper.map(user, SignupResponse.class);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("Helloworld")).thenReturn("Helloworld");
        when(userRepository.save(any())).thenReturn(user);

        SignupResponse actualResponse = userService.userSignup(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testLogin() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 0.0);
        LoginRequest request = new LoginRequest("sharok@gmail.com", "Helloworld");
        LoginResponse expectedResponse = new ModelMapper().map(request, LoginResponse.class);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(!passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        LoginResponse actualResponse = userService.userLogin(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateAccountBalance() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 0.0);
        AccountBalanceRequest request = new AccountBalanceRequest(100.0);
        User updatedAmount = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 100.0);
        AccountBalanceResponse expectedResponse =
                new ModelMapper().map(updatedAmount, AccountBalanceResponse.class);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);

        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedAmount);

        AccountBalanceResponse actualResponse = userService.updateAccountBalance(request);

        assertEquals(expectedResponse, actualResponse);
    }
}
