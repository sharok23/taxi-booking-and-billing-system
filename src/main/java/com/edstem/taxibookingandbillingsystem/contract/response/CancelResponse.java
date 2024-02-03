package com.edstem.taxibookingandbillingsystem.contract.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CancelResponse {
    private String cancel;
}
