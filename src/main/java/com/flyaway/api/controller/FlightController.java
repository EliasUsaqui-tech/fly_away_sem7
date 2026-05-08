package com.flyaway.api.controller;

import com.flyaway.api.dto.*;
import com.flyaway.api.entity.Flight;
import com.flyaway.api.entity.User;
import com.flyaway.api.service.BookingService;
import com.flyaway.api.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@Valid @RequestBody NewFlightRequestDTO dto) {
        return ResponseEntity.status(201).body(flightService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getById(@PathVariable String id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {
        return ResponseEntity.ok(flightService.search(
                flightNumber, airlineName, estDepartureTimeFrom, estDepartureTimeTo));
    }

    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(
            @Valid @RequestBody FlightBookRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(bookingService.book(dto, currentUser));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
    @PostMapping("/create-many")
    public ResponseEntity<NewFlightManyResponseDTO> createMany(
            @RequestBody NewFlightManyRequestDTO dto) {
        flightService.createMany(dto.getInputs());
        return ResponseEntity.status(201).body(new NewFlightManyResponseDTO(List.of()));
    }
}