package com.edstem.taxibookingandbillingsystem.contract.request;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountBalanceRequest {
    private Double accountBalance;
}
