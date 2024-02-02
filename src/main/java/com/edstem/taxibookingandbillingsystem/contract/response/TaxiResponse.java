package com.edstem.taxibookingandbillingsystem.contract.response;

import lombok.Getter;

@Getter
public class TaxiResponse {
    private Long id;
    private String driverName;
    private String licenseNumber;
    private String currentLocation;
}
