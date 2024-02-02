package com.edstem.taxibookingandbillingsystem.contract.response;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long taxiId;
    private String pickupLocation;
    private String dropoffLocation;
    private Double fare;
    private LocalDateTime bookingTime;
    private Status status;
}
