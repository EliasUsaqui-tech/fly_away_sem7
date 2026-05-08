package com.flyaway.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String airlineName;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private Instant estDepartureTime;

    @Column(nullable = false)
    private Instant estArrivalTime;

    @Column(nullable = false)
    private Integer availableSeats;
}