package com.flyaway.api.service;
import org.springframework.scheduling.annotation.Async;
import com.flyaway.api.dto.NewFlightRequestDTO;
import com.flyaway.api.dto.NewIdDTO;
import com.flyaway.api.entity.Flight;
import com.flyaway.api.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public NewIdDTO create(NewFlightRequestDTO dto) {
        if (dto.getEstDepartureTime().isAfter(dto.getEstArrivalTime()) ||
                dto.getEstDepartureTime().equals(dto.getEstArrivalTime())) {
            throw new RuntimeException("Departure time must be before arrival time");
        }

        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new RuntimeException("Flight number already exists");
        }

        Flight flight = new Flight();
        flight.setAirlineName(dto.getAirlineName());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEstDepartureTime(dto.getEstDepartureTime());
        flight.setEstArrivalTime(dto.getEstArrivalTime());
        flight.setAvailableSeats(dto.getAvailableSeats());

        Flight saved = flightRepository.save(flight);
        return new NewIdDTO(saved.getId());
    }

    public List<Flight> search(String flightNumber, String airlineName,
                               String from, String to) {
        Instant fromInstant = (from != null && !from.isBlank()) ? Instant.parse(from) : null;
        Instant toInstant = (to != null && !to.isBlank()) ? Instant.parse(to) : null;

        return flightRepository.search(
                (flightNumber != null && !flightNumber.isBlank()) ? flightNumber : null,
                (airlineName != null && !airlineName.isBlank()) ? airlineName : null,
                fromInstant,
                toInstant
        );
    }

    public Flight findById(String id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    public void deleteAll() {
        flightRepository.deleteAll();
    }

    public void save(Flight flight) {
        flightRepository.save(flight);
    }

    @Async
    public void createMany(List<NewFlightRequestDTO> inputs) {
        for (NewFlightRequestDTO dto : inputs) {
            try {
                create(dto);
            } catch (Exception e) {
                // skip invalid
            }
        }
    }
}