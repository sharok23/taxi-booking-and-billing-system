package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.CancelResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.exception.InsufficientBalanceException;
import com.edstem.taxibookingandbillingsystem.model.Booking;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.BookingRepository;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TaxiRepository taxiRepository;

    public List<TaxiResponse> searchNearestTaxi(Long userId, String pickupLocation) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        List<Taxi> allTaxies = taxiRepository.findAll();
        List<Taxi> availableTaxies = new ArrayList<>();
        for (Taxi taxies : allTaxies) {
            if (taxies.getCurrentLocation().equals(pickupLocation)) {
                availableTaxies.add(taxies);
            }
        }
        if (availableTaxies.isEmpty()) {
            throw new EntityNotFoundException("Taxi");
        } else {
            return availableTaxies.stream().map(taxi -> modelMapper.map(taxi, TaxiResponse.class)).collect(Collectors.toList());
        }

    }

    public BookingResponse bookTaxi(BookingRequest request, Long userId, Long taxiId, Long distance) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new EntityNotFoundException("Taxi", taxiId));
        Double expense = distance * 10d;
        if (expense > user.getAccountBalance()) {
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
                .accountBalance(user.getAccountBalance() - savedBooking.getFare())
                .build();
        balanceAmount = userRepository.save(balanceAmount);
        savedBooking = bookingRepository.save(savedBooking);
        return modelMapper.map(savedBooking, BookingResponse.class);
    }

    public BookingResponse viewBookingDetail(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Booking", bookingId));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        if (userId != booking.getUser().getId()) {
            throw new EntityNotFoundException("Booking");
        }
        booking = modelMapper.map(booking, Booking.class);
        return modelMapper.map(booking, BookingResponse.class);

    }

    public CancelResponse cancelBooking(Long bookingId, Long userId, Long taxiId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Booking", bookingId));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Taxi taxi = taxiRepository.findById(taxiId).orElseThrow(() -> new EntityNotFoundException("Taxi", taxiId));
        Booking bookings = Booking.builder()
                .id(bookingId)
                .user(user)
                .taxi(taxi)
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .fare(booking.getFare())
                .status(Status.CANCELLED)
                .build();
        bookingRepository.save(bookings);
        return CancelResponse.builder()
                .cancel("Booked taxi has been cancelled")
                .build();
    }
}


