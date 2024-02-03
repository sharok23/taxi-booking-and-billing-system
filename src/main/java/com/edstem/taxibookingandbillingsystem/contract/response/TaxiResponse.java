package com.edstem.taxibookingandbillingsystem.contract.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class TaxiResponse {
    private Long id;
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
