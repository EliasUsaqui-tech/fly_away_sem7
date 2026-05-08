package com.flyaway.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.Instant;

@Data
public class NewFlightRequestDTO {

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$", message = "Flight number format invalid")
    private String flightNumber;

    @NotNull(message = "Departure time is required")
    private Instant estDepartureTime;

    @NotNull(message = "Arrival time is required")
    private Instant estArrivalTime;

    @NotNull(message = "Available seats is required")
    @Min(value = 1, message = "Available seats must be greater than 0")
    private Integer availableSeats;
}