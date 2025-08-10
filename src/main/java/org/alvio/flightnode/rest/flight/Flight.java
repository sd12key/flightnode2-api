package org.alvio.flightnode.rest.flight;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.aircraft.Aircraft;
import org.alvio.flightnode.rest.airport.Airport;
import org.alvio.flightnode.rest.gate.Gate;
import org.alvio.flightnode.rest.passenger.Passenger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number is required.")
    @Size(min = 3, max = 8, message = "Flight number must be 3-8 characters.") // Matches regex
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{1,4}[A-Z]?$",
            message = "Invalid format. Examples: AA123, UAL4567, LH123A.")
    @Column(nullable = false, length = 8)
    private String flightNumber;

    @NotNull(message = "Departure date and time is required.")
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival date and time is required")
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_gate_id", nullable = false)
    private Gate departureGate;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_gate_id", nullable = false)
    private Gate arrivalGate;

    @ManyToMany(mappedBy = "flights", fetch = FetchType.EAGER)
    private List<Passenger> passengers = new ArrayList<>();

    // validate arrival and departure time
    @AssertTrue(message = "Flight duration must be between 30 minutes and 24 hours.")
    public boolean isValidDuration() {
        if (arrivalTime == null || departureTime == null) return false;
        Duration d = Duration.between(departureTime, arrivalTime);
        return !d.isNegative() && d.toMinutes() >= 30 && d.toHours() <= 24;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber.trim().toUpperCase();
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Gate getDepartureGate() { return departureGate; }

    public void setDepartureGate(Gate departureGate) { this.departureGate = departureGate; }

    public Gate getArrivalGate() { return arrivalGate; }

    public void setArrivalGate(Gate arrivalGate) { this.arrivalGate = arrivalGate; }

    public List<Passenger> getPassengers() { return passengers; }

}
