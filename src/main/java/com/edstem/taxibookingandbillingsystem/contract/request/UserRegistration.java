package com.edstem.taxibookingandbillingsystem.contract.request;

import lombok.Getter;

@Getter
public class UserRegistration {
    private String name;
    private String email;
    private String password;
    private Double accountBalance;
}
