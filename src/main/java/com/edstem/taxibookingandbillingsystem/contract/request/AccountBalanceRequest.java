package com.edstem.taxibookingandbillingsystem.contract.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceRequest {
    @NotNull(message = "Amount should not be blank")
    private Double accountBalance;
}
