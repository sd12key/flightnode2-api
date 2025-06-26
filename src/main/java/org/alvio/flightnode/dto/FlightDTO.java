package org.alvio.flightnode.dto;

import java.time.LocalDateTime;

public class FlightDTO {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private AircraftSummaryDTO aircraft;
    private AirportSummaryDTO departureAirport;
    private AirportSummaryDTO arrivalAirport;

    public FlightDTO(Long id, String flightNumber,
                     LocalDateTime departureTime, LocalDateTime arrivalTime,
                     AircraftSummaryDTO aircraft,
                     AirportSummaryDTO departureAirport, AirportSummaryDTO arrivalAirport
                     ) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    // Getters only (for output)
    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public AircraftSummaryDTO getAircraft() { return aircraft; }
    public AirportSummaryDTO getDepartureAirport() { return departureAirport; }
    public AirportSummaryDTO getArrivalAirport() { return arrivalAirport; }
}
