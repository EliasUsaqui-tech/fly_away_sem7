package com.flyaway.api.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class BookingResponseDTO {
    private String id;
    private Instant bookingDate;
    private String flightId;
    private String flightNumber;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private Instant estDepartureTime;
    private Instant estArrivalTime;
}