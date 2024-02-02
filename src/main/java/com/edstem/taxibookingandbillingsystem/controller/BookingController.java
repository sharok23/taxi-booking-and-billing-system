package com.edstem.taxibookingandbillingsystem.controller;

import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.CancelResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
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

import java.util.List;


@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/searchTaxi")
    public List<TaxiResponse> searchNearestTaxi(@RequestParam Long userId, @RequestParam String pickupLocation) {
        return bookingService.searchNearestTaxi(userId, pickupLocation);
    }
    @PostMapping
    public BookingResponse bookTaxi(@Valid @RequestBody BookingRequest request, @RequestParam Long userId, @RequestParam Long taxiId,@RequestParam Long distance) {
        return bookingService.bookTaxi(request, userId, taxiId,distance);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse viewBookingDetail(@PathVariable Long bookingId,@RequestParam Long userId) {
        return bookingService.viewBookingDetail(bookingId,userId);
    }

    @PutMapping("/cancel/{bookingId}")
    public CancelResponse cancelBooking(@PathVariable Long bookingId, @RequestParam Long userId, @RequestParam Long taxiId) {
        return bookingService.cancelBooking(bookingId,userId,taxiId);
    }
}
