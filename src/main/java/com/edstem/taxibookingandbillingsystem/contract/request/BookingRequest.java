package com.edstem.taxibookingandbillingsystem.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingRequest {
    @NotBlank(message = "Pickup location should not be blank")
    private String pickupLocation;

    @NotBlank(message = "Drop-off location should not be blank")
    private String dropoffLocation;
}
