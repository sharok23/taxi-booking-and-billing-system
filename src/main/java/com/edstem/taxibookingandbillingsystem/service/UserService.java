package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.contract.request.AccountBalanceRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.LoginRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.AccountBalanceResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.LoginResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.SignupResponse;
import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.exception.InvalidUserException;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import com.edstem.taxibookingandbillingsystem.security.JwtService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse userSignup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidUserException("signup");
        }
        User user =
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .accountBalance(0D)
                        .build();
        userRepository.save(user);
        return modelMapper.map(user, SignupResponse.class);
    }

    public LoginResponse userLogin(LoginRequest request) {
        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new InvalidUserException("Login"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidUserException("Login");
        }
        String jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder().token(jwtToken).build();
    }

    public AccountBalanceResponse updateAccountBalance(AccountBalanceRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getEmail();
        user =
                userRepository
                        .findByEmail(userEmail)
                        .orElseThrow(() -> new EntityNotFoundException("User"));
        user =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .accountBalance(user.getAccountBalance() + request.getAccountBalance())
                        .build();
        User updatedAccountBalance = userRepository.save(user);
        return modelMapper.map(updatedAccountBalance, AccountBalanceResponse.class);
    }
}
