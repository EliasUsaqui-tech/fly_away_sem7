package com.flyaway.api.controller;

import com.flyaway.api.dto.BookingResponseDTO;
import com.flyaway.api.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
}