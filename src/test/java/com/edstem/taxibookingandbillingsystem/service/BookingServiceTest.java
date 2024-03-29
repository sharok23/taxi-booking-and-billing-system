package com.edstem.taxibookingandbillingsystem.service;

import static com.edstem.taxibookingandbillingsystem.constant.Status.BOOKED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.CancelResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.exception.BookingAlreadyCancelledException;
import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.exception.InsufficientBalanceException;
import com.edstem.taxibookingandbillingsystem.model.Booking;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.BookingRepository;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class BookingServiceTest {
    private BookingRepository bookingRepository;
    private ModelMapper modelMapper;
    private BookingService bookingService;
    private TaxiRepository taxiRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookingRepository = Mockito.mock(BookingRepository.class);
        taxiRepository = Mockito.mock(TaxiRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = new ModelMapper();
        bookingService =
                new BookingService(bookingRepository, modelMapper, userRepository, taxiRepository);
    }

    @Test
    void testSearchNearestTaxi() {
        Taxi taxiOne = new Taxi(1L, "Midun", "KL 01 5508", "Kakkanad");
        Taxi taxiTwo = new Taxi(1L, "Dathan", "KL 03 8804", "Kakkanad");

        List<Taxi> availableTaxies = Arrays.asList(taxiOne, taxiTwo);
        when(taxiRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(
                EntityNotFoundException.class, () -> bookingService.searchNearestTaxi("Kakkanad"));
        when(taxiRepository.findAll()).thenReturn(availableTaxies);

        List<TaxiResponse> expectedResponse =
                availableTaxies.stream()
                        .map(taxi -> modelMapper.map(taxi, TaxiResponse.class))
                        .collect(Collectors.toList());

        List<TaxiResponse> actualResponse = bookingService.searchNearestTaxi("Kakkanad");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testBookTaxi() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 1000.0);
        Taxi taxi =
                Taxi.builder()
                        .driverName("Midun")
                        .licenseNumber("KL 01 5508")
                        .currentLocation("Kakkanad")
                        .build();

        BookingRequest request = new BookingRequest("Kakkanad", "Aluva");
        Long taxiId = 1L;
        Long distance = 80L;
        Double expense = distance * 10D;
        Booking expectedBooking =
                Booking.builder()
                        .user(user)
                        .taxi(taxi)
                        .pickupLocation(request.getPickupLocation())
                        .dropoffLocation(request.getDropoffLocation())
                        .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .fare(expense)
                        .status(BOOKED)
                        .build();
        User updatedUser =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .accountBalance(user.getAccountBalance() - expectedBooking.getFare())
                        .build();

        BookingResponse expectedResponse = modelMapper.map(expectedBooking, BookingResponse.class);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);

        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);
        when(userRepository.save(any())).thenReturn(updatedUser);

        BookingResponse actualResponse = bookingService.bookTaxi(request, taxiId, distance);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testViewBookingDetail() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 1000.0);
        Taxi taxi = new Taxi(1L, "Midun", "KL 01 5508", "Kakkanad");
        BookingRequest request = new BookingRequest("Kakkanad", "Aluva");
        Long bookingId = 1L;
        Long distance = 80L;
        Double expense = distance * 10.0;
        Booking expectedBooking =
                Booking.builder()
                        .id(bookingId)
                        .user(user)
                        .taxi(taxi)
                        .pickupLocation(request.getPickupLocation())
                        .dropoffLocation(request.getDropoffLocation())
                        .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .fare(expense)
                        .status(BOOKED)
                        .build();
        BookingResponse expectedResponse = modelMapper.map(expectedBooking, BookingResponse.class);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(expectedBooking.getId()))
                .thenReturn(Optional.of(expectedBooking));

        BookingResponse actualResponse = bookingService.viewBookingDetail(bookingId);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCancelBooking() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 1000.0);
        Taxi taxi = new Taxi(1L, "Midun", "KL 01 5508", "Kakkanad");
        Long taxiId = 1L;
        Long bookingId = 1L;
        Long distance = 80L;
        Double expense = distance * 10D;
        Booking expectedBooking =
                Booking.builder()
                        .id(bookingId)
                        .user(user)
                        .taxi(taxi)
                        .pickupLocation("Aluva")
                        .dropoffLocation("Kakkanad")
                        .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .fare(expense)
                        .status(BOOKED)
                        .build();

        CancelResponse expectedResponse =
                CancelResponse.builder().cancel("Booked taxi has been cancelled").build();

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);

        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);

        CancelResponse actualResponse = bookingService.cancelBooking(bookingId, taxiId);
        assertEquals(expectedResponse, actualResponse);

        when(taxiRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.cancelBooking(bookingId, taxiId));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.cancelBooking(bookingId, taxiId));

        when(taxiRepository.findById(taxiId)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.cancelBooking(bookingId, taxiId));
        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));

        Booking cancelledBooking =
                Booking.builder()
                        .id(expectedBooking.getId())
                        .user(expectedBooking.getUser())
                        .taxi(expectedBooking.getTaxi())
                        .pickupLocation(expectedBooking.getPickupLocation())
                        .dropoffLocation(expectedBooking.getDropoffLocation())
                        .bookingTime(expectedBooking.getBookingTime())
                        .fare(expectedBooking.getFare())
                        .status(Status.CANCELLED)
                        .build();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(cancelledBooking));
        assertThrows(
                BookingAlreadyCancelledException.class,
                () -> bookingService.cancelBooking(bookingId, taxiId));
    }

    @Test
    void testBookTaxi_InsufficientBalance() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 500.0);
        Taxi taxi =
                Taxi.builder()
                        .driverName("Midun")
                        .licenseNumber("KL 01 5508")
                        .currentLocation("Kakkanad")
                        .build();

        BookingRequest request = new BookingRequest("Kakkanad", "Aluva");
        Long taxiId = 1L;
        Long distance = 80L;

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);

        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));

        assertThrows(
                InsufficientBalanceException.class,
                () -> {
                    bookingService.bookTaxi(request, taxiId, distance);
                });
    }
}
