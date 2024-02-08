package com.edstem.taxibookingandbillingsystem.exception;

public class BookingAlreadyCancelledException extends RuntimeException {
    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
