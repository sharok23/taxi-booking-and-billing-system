package com.edstem.taxibookingandbillingsystem.controller;

import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponse bookTaxi(@Valid @RequestBody BookingRequest request, @RequestParam Long userId, @RequestParam Long taxiId,@RequestParam Long distance) {
        return bookingService.bookTaxi(request, userId, taxiId,distance);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse viewBookingDetail(@PathVariable Long bookingId) {
        return bookingService.viewBookingDetail(bookingId);
    }

//    @PutMapping("/{bookingId}")
//    public BookingResponse viewBookingDetail(@PathVariable Long bookingId) {
//        return bookingService.viewBookingDetail(bookingId);
//    }
}
