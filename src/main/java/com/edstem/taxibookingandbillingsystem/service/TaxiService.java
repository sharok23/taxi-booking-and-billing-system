package com.edstem.taxibookingandbillingsystem.service;

import com.edstem.taxibookingandbillingsystem.contract.request.TaxiRequest;
import com.edstem.taxibookingandbillingsystem.contract.response.TaxiResponse;
import com.edstem.taxibookingandbillingsystem.exception.EntityAlreadyExistsException;
import com.edstem.taxibookingandbillingsystem.exception.InvalidUserException;
import com.edstem.taxibookingandbillingsystem.model.Taxi;
import com.edstem.taxibookingandbillingsystem.repository.TaxiRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaxiService {
    private final TaxiRepository taxiRepository;
    private final ModelMapper modelMapper;

    public TaxiResponse addTaxi(TaxiRequest request) {
        if (taxiRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new EntityAlreadyExistsException("License number");
        }
        Taxi taxi = modelMapper.map(request, Taxi.class);
        taxi = taxiRepository.save(taxi);
        return modelMapper.map(taxi, TaxiResponse.class);
    }
}

