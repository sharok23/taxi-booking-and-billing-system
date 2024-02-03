package com.edstem.taxibookingandbillingsystem.contract.response;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long taxiId;
    private String pickupLocation;
    private String dropoffLocation;
    private Double fare;
    private String bookingTime;
    private Status status;
}
