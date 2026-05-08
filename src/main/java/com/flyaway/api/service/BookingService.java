package com.flyaway.api.service;
import java.io.FileWriter;
import java.io.IOException;
import com.flyaway.api.dto.BookingResponseDTO;
import com.flyaway.api.dto.FlightBookRequestDTO;
import com.flyaway.api.dto.NewIdDTO;
import com.flyaway.api.entity.Booking;
import com.flyaway.api.entity.Flight;
import com.flyaway.api.entity.User;
import com.flyaway.api.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;

    public NewIdDTO book(FlightBookRequestDTO dto, User currentUser) {
        Flight flight = flightService.findById(dto.getFlightId());

        // No reservar vuelos pasados o en tránsito
        Instant now = Instant.now();
        if (flight.getEstArrivalTime().isBefore(now)) {
            throw new RuntimeException("Cannot book a past or in-transit flight");
        }

        // No reservar vuelos que se solapan
        List<Booking> userBookings = bookingRepository.findByUserId(currentUser.getId());
        for (Booking existing : userBookings) {
            Flight existingFlight = existing.getFlight();
            boolean overlaps = flight.getEstDepartureTime().isBefore(existingFlight.getEstArrivalTime())
                    && flight.getEstArrivalTime().isAfter(existingFlight.getEstDepartureTime());
            if (overlaps) {
                throw new RuntimeException("Flight overlaps with an existing booking");
            }
        }

        // No sobrevender
        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightService.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(currentUser);
        booking.setBookingDate(Instant.now());

        Booking saved = bookingRepository.save(booking);
        sendEmailFile(saved);
        return new NewIdDTO(saved.getId());
    }

    public BookingResponseDTO findById(String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return toDTO(booking);
    }

    private BookingResponseDTO toDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setFlightId(booking.getFlight().getId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setCustomerId(booking.getUser().getId());
        dto.setCustomerFirstName(booking.getUser().getFirstName());
        dto.setCustomerLastName(booking.getUser().getLastName());
        dto.setEstDepartureTime(booking.getFlight().getEstDepartureTime());
        dto.setEstArrivalTime(booking.getFlight().getEstArrivalTime());
        return dto;
    }
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    private void sendEmailFile(Booking booking) {
        String path = "C:\\Users\\HP\\Desktop\\-cs2031-2026-1-week07-tester\\flight_booking_email_" + booking.getId() + ".txt";

        String content = "Hello " + booking.getUser().getFirstName() + " " + booking.getUser().getLastName() + ",\n\n" +
                "Your booking was successful! \n\n" +
                "The booking is for flight " + booking.getFlight().getFlightNumber() +
                " with departure date of " + booking.getFlight().getEstDepartureTime().toString() +
                " and arrival date of " + booking.getFlight().getEstArrivalTime().toString() + ".\n\n" +
                "The booking was registered at " + booking.getBookingDate().toString() + ".\n\n" +
                "Bon Voyage!\n" +
                "Fly Away Travel";

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}