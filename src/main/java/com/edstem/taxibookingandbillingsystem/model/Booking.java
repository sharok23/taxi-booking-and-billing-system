package com.edstem.taxibookingandbillingsystem.model;

import com.edstem.taxibookingandbillingsystem.constant.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pickupLocation;
    private String dropoffLocation;
    private Double fare;
    private LocalDateTime bookingTime;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private User user;
    @ManyToOne
    private Taxi taxi;
}
