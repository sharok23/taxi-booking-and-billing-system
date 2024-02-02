package com.edstem.taxibookingandbillingsystem.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TaxiRequest {
    @NotBlank(message = "Driver name should not be blank")
    private String driverName;
    @NotBlank(message = "License number should not be blank")
    private String licenseNumber;
    @NotBlank(message = "Current location should not be blank")
    private String currentLocation;
}
