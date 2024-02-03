package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.AccountBalanceResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.model.Booking;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.BookingRepository;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edstem.taxibookingandbillingsystem.constant.Status.BOOKED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        bookingService = new BookingService(bookingRepository,modelMapper,userRepository,taxiRepository);
    }

    @Test
    void testSearchNearestTaxi() {
        Taxi taxiOne = new Taxi(1L,"Midun","KL 01 5508","Kakkanad");
        Taxi taxiTwo = new Taxi(1L,"Dathan","KL 03 8804","Kakkanad");
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 0.0);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);

        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        List<Taxi> availableTaxies = Arrays.asList(taxiOne, taxiTwo);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        List<TaxiResponse> expectedResponse = availableTaxies.stream().map(taxi -> modelMapper.map(taxi, TaxiResponse.class)).collect(Collectors.toList());
        when(taxiRepository.findAll()).thenReturn(availableTaxies);
        List<TaxiResponse> actualResponse = bookingService.searchNearestTaxi("Kakkanad");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testBookTaxi() {
        User user = new User(1L, "Sharok", "sharok@gmail.com", "Helloworld", 1000.0);
        Taxi taxi = new Taxi(1L,"Midun","KL 01 5508","Kakkanad");
        BookingRequest request = new BookingRequest("Kakkanad","Aluva");
        Long taxiId = 1L;
        Long distance = 80L;
        Double expense = distance * 10D;
        Booking expectedBooking = Booking.builder()
                .user(user)
                .taxi(taxi)
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                .fare(expense)
                .status(BOOKED)
                .build();
        User updatedUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .accountBalance(user.getAccountBalance() - expectedBooking.getFare())
                .build();

        BookingResponse expectedResponse = modelMapper.map(expectedBooking,BookingResponse.class);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);

        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);
        when(userRepository.save(any())).thenReturn(updatedUser);

        BookingResponse actualResponse = bookingService.bookTaxi(request,taxiId,distance);
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void testViewBookingDetail(){
        BookingResponse booking  = new Booking(1L,"Aluva","Kakkanad",50D,"2024-02-03 10:18:28.012173",BOOKED,1L,1L);

    }



}
