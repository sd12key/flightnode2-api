package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassengerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private List<FlightSummaryDTO> bookings;
    private List<AirportSummaryDTO> airports;
    private List<AircraftSummaryDTO> aircrafts;

    public PassengerDTO(Long id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }

    public List<FlightSummaryDTO> getBookings() { return bookings; }
    public void setBookings(List<FlightSummaryDTO> bookings) { this.bookings = bookings; }

    public List<AirportSummaryDTO> getAirports() { return airports; }
    public void setAirports(List<AirportSummaryDTO> airports) { this.airports = airports; }

    public List<AircraftSummaryDTO> getAircrafts() { return aircrafts; }
    public void setAircrafts(List<AircraftSummaryDTO> aircrafts) { this.aircrafts = aircrafts; }
}
