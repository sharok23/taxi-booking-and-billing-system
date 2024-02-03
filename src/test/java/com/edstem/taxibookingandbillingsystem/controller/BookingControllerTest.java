package com.edstem.taxibookingandbillingsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import com.edstem.taxibookingandbillingsystem.contract.request.BookingRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.BookingResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.CancelResponse;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private BookingService bookingService;

    @Test
    void testSearchNearestTaxi() throws Exception {
        String pickupLocation = "Aluva";
        List<TaxiResponse> expectedResponse =
                Collections.singletonList(new TaxiResponse(1L, "Midun", "KL 01 5508", "Aluva"));

        when(bookingService.searchNearestTaxi(pickupLocation)).thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/book/searchTaxi")
                                .param("pickupLocation", String.valueOf(pickupLocation))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testBookTaxi() throws Exception {
        BookingRequest bookingRequest = new BookingRequest("Aluva", "Kakkanad");
        Long taxiId = 1L;
        Long distance = 80L;
        BookingResponse expectedResponse =
                new BookingResponse(
                        1L,
                        1L,
                        1L,
                        "Aluva",
                        "Kakkanad",
                        800.0,
                        "2024-02-03 10:18:28.012173",
                        Status.BOOKED);

        when(bookingService.bookTaxi(any(BookingRequest.class), anyLong(), anyLong()))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/book")
                                .param("taxiId", String.valueOf(taxiId))
                                .param("distance", String.valueOf(distance))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testViewBookingDetail() throws Exception {
        Long bookingId = 1L;
        BookingResponse expectedResponse =
                new BookingResponse(
                        bookingId,
                        1L,
                        1L,
                        "Aluva",
                        "Kakkanad",
                        800.0,
                        "2024-02-03 10:18:28.012173",
                        Status.BOOKED);

        when(bookingService.viewBookingDetail(bookingId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/book/{bookingId}", bookingId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testCancelBooking() throws Exception {
        Long bookingId = 1L;
        Long taxiId = 1L;
        CancelResponse expectedResponse =
                CancelResponse.builder().cancel("Booked taxi has been cancelled").build();

        when(bookingService.cancelBooking(anyLong(), anyLong())).thenReturn(expectedResponse);

        mockMvc.perform(
                        put("/book/cancel/{bookingId}", bookingId)
                                .param("taxiId", String.valueOf(taxiId))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}
