package com.edstem.taxibookingandbillingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.edstem.taxibookingandbillingsystem.contract.request.TaxiRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.exception.EntityAlreadyExistsException;
import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class TaxiServiceTest {
    private TaxiRepository taxiRepository;
    private ModelMapper modelMapper;
    private TaxiService taxiService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        taxiRepository = Mockito.mock(TaxiRepository.class);
        modelMapper = new ModelMapper();
        taxiService = new TaxiService(taxiRepository, modelMapper);
    }

    @Test
    void testAddTaxi() {
        TaxiRequest request = new TaxiRequest("Midun", "KL O1 5581", "Kakkanad");
        Taxi taxi = modelMapper.map(request, Taxi.class);
        TaxiResponse expectedResponse = modelMapper.map(taxi, TaxiResponse.class);

        when(taxiRepository.existsByLicenseNumber(request.getLicenseNumber())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> taxiService.addTaxi(request));

        when(taxiRepository.existsByLicenseNumber(request.getLicenseNumber())).thenReturn(false);
        when(taxiRepository.save(any())).thenReturn(taxi);

        TaxiResponse actualResponse = taxiService.addTaxi(request);

        assertEquals(expectedResponse, actualResponse);
    }
}
