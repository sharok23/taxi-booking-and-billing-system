package com.edstem.taxibookingandbillingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BookingAlreadyCancelledException extends RuntimeException {
    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
