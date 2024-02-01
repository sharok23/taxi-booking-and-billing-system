package com.edstem.taxibookingandbillingsystem.contract.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {
    @NotBlank(message = "Name should not be blank")
    private String name;
    @NotBlank(message = "Email should not be blank")
    @Email
    private String email;
    @NotBlank(message = "Password should not be blank")
    private String password;
    private Double accountBalance;
}
