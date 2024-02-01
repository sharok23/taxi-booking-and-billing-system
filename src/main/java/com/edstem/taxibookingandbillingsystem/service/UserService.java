package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.SignupResponse;
import com.edstem.taxibookingandbillingsystem.exception.InvalidUserException;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public SignupResponse userSignup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidUserException("signup");
        }
        User user = modelMapper.map(request, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(user,SignupResponse.class);
    }
}
