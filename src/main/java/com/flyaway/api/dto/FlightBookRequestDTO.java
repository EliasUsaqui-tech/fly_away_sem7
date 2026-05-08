package com.flyaway.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlightBookRequestDTO {

    @NotBlank(message = "Flight ID is required")
    private String flightId;
}