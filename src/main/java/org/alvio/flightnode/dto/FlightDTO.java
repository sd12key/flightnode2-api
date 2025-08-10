package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private AircraftSummaryDTO aircraft;
    private AirportSummaryDTO departureAirport;
    private GateSummaryDTO departureGate;
    private AirportSummaryDTO arrivalAirport;
    private GateSummaryDTO arrivalGate;
    private List<PassengerSummaryDTO> passengers;

    public FlightDTO(Long id, String flightNumber,
                     LocalDateTime departureTime, LocalDateTime arrivalTime,
                     AircraftSummaryDTO aircraft,
                     AirportSummaryDTO departureAirport,
                     GateSummaryDTO departureGate,
                     AirportSummaryDTO arrivalAirport,
                     GateSummaryDTO arrivalGate
                     ) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
        this.departureAirport = departureAirport;
        this.departureGate = departureGate;
        this.arrivalAirport = arrivalAirport;
        this.arrivalGate = arrivalGate;
    }

    // Getters only (for output)
    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public AircraftSummaryDTO getAircraft() { return aircraft; }
    public AirportSummaryDTO getDepartureAirport() { return departureAirport; }
    public AirportSummaryDTO getArrivalAirport() { return arrivalAirport; }

    public GateSummaryDTO getDepartureGate() { return departureGate; }
    public GateSummaryDTO getArrivalGate() { return arrivalGate; }

    public List<PassengerSummaryDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerSummaryDTO> passengers) {
        this.passengers = passengers;
    }

}
