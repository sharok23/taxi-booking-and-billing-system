package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.contract.request.AccountBalanceRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public SignupResponse userSignup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidUserException("signup");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountBalance(request.getAccountBalance())
                .build();
        User savedUser = userRepository.save(user);
        return modelMapper.map(user,SignupResponse.class);
    }

    public LoginResponse userLogin(LoginRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidUserException("Login"));
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidUserException("Login");
            }
        String jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AccountBalanceResponse updateAccountBalance(Long id, AccountBalanceRequest request) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User", id));
        modelMapper.map(request, user);
        User updatedAccountBalance=userRepository.save(user);
        return modelMapper.map(updatedAccountBalance, AccountBalanceResponse.class);
    }
}
