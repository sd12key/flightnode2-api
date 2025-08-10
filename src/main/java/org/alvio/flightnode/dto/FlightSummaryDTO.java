package org.alvio.flightnode.dto;

import java.time.LocalDateTime;

public class FlightSummaryDTO {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String aircraftType;
    private String airlineName;

    public FlightSummaryDTO(Long id, String flightNumber,
                         LocalDateTime departureTime, LocalDateTime arrivalTime,
                         String departureAirportCode, String arrivalAirportCode,
                         String aircraftType, String airlineName) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirportCode = departureAirportCode;

        this.arrivalAirportCode = arrivalAirportCode;
        this.aircraftType = aircraftType;
        this.airlineName = airlineName;
    }

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public String getDepartureAirportCode() { return departureAirportCode; }
    public String getArrivalAirportCode() { return arrivalAirportCode; }
    public String getAircraftType() { return aircraftType; }
    public String getAirlineName() { return airlineName; }

}
