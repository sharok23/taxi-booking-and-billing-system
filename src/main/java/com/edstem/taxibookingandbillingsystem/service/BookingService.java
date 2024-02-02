package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.exception.InsufficientBalanceException;
import com.edstem.taxibookingandbillingsystem.model.Booking;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.BookingRepository;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TaxiRepository taxiRepository;
    public BookingResponse bookTaxi(BookingRequest request, Long userId, Long taxiId,Long distance) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User",userId));
        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new EntityNotFoundException("Taxi",taxiId));
        Double expense = distance * 10d;
        if (expense>user.getAccountBalance()){
            throw new InsufficientBalanceException();
        }
        Booking savedBooking = Booking.builder()
                .user(user)
                .taxi(taxi)
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .fare(expense)
                .status(Status.BOOKED)
                .build();
        User balanceAmount = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .accountBalance(user.getAccountBalance()-savedBooking.getFare())
                .build();
        balanceAmount = userRepository.save(balanceAmount);
        savedBooking = bookingRepository.save(savedBooking);
        return modelMapper.map(savedBooking, BookingResponse.class);
    }

    public BookingResponse viewBookingDetail(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Booking",bookingId));
        booking = Booking.builder()
                .id(bookingId)
                .taxi(booking.getTaxi())
                .user(booking.getUser())
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .fare(null)
                .bookingTime(booking.getBookingTime())
                .status(booking.getStatus())
                .build();
        return modelMapper.map(booking, BookingResponse.class);
    }
}
