package com.edstem.taxibookingandbillingsystem.contract.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Double accountBalance;
}
