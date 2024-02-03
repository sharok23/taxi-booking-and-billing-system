package com.edstem.taxibookingandbillingsystem.controller;

import com.edstem.taxibookingandbillingsystem.contract.request.AccountBalanceRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.LoginRequest;
import com.edstem.taxibookingandbillingsystem.contract.request.SignupRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.AccountBalanceResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.LoginResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.SignupResponse;
import com.edstem.taxibookingandbillingsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public SignupResponse userSignup(@Valid @RequestBody SignupRequest request){
        return userService.userSignup(request);
    }

    @PostMapping("/user/login")
    public LoginResponse userLogin(@Valid @RequestBody LoginRequest request){
        return userService.userLogin(request);
    }

//    @PutMapping("/{id}")
//    public AccountBalanceResponse updateAccountBalance(@PathVariable Long id,@RequestBody AccountBalanceRequest request){
//        return userService.updateAccountBalance(id,request);
//    }
    @PutMapping()
    public AccountBalanceResponse updateAccountBalance(@RequestBody AccountBalanceRequest request){
    return userService.updateAccountBalance(request);
}
}
