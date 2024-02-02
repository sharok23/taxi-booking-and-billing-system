package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Taxi> availableTaxies = Arrays.asList(taxiOne, taxiTwo);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        List<TaxiResponse> expectedResponse = availableTaxies.stream().map(taxi -> modelMapper.map(taxi, TaxiResponse.class)).collect(Collectors.toList());
        when(taxiRepository.findAll()).thenReturn(availableTaxies);
        List<TaxiResponse> actualResponse = bookingService.searchNearestTaxi(user.getId(),"Kakkanad");
        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    void testBookTaxi() {
//        Long id =1L;
//        BookingRequest request = new BookingRequest("Kakkanad","Aluva");
//        Booking booking = modelMapper.map(request, Booking.class);
//        BookingResponse expectedResponse = modelMapper.map(song, BookingResponse.class);
//
//        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
//        when(taxiRepository.findById(id)).thenReturn(Optional.of(taxi));
//        when(songRepository.save(any())).thenReturn(song);
//
//        SongResponse actualResponse = songService.addSong(request);
//
//        assertEquals(expectedResponse, actualResponse);
//    }



}
