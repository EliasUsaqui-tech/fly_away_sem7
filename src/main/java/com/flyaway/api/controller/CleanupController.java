package com.flyaway.api.controller;

import com.flyaway.api.service.BookingService;
import com.flyaway.api.service.FlightService;
import com.flyaway.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleanup")
@RequiredArgsConstructor
public class CleanupController {

    private final UserService userService;
    private final FlightService flightService;
    private final BookingService bookingService;

    @DeleteMapping
    public ResponseEntity<Void> cleanup() {
        bookingService.deleteAll();
        flightService.deleteAll();
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }
}