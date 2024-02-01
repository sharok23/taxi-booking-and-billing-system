package com.edstem.taxibookingandbillingsystem.contract.request;

import lombok.Getter;

@Getter
public class BookingRequest {
    private String pickupLocation;
    private String dropoffLocation;
}
