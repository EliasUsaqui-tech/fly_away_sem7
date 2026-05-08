package com.flyaway.api.repository;

import com.flyaway.api.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, String> {
    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:flightNumber IS NULL OR LOWER(f.flightNumber) LIKE LOWER(CONCAT('%', :flightNumber, '%'))) AND " +
            "(:airlineName IS NULL OR LOWER(f.airlineName) LIKE LOWER(CONCAT('%', :airlineName, '%'))) AND " +
            "(:from IS NULL OR f.estDepartureTime >= :from) AND " +
            "(:to IS NULL OR f.estDepartureTime <= :to)")
    List<Flight> search(@Param("flightNumber") String flightNumber,
                        @Param("airlineName") String airlineName,
                        @Param("from") Instant from,
                        @Param("to") Instant to);
}