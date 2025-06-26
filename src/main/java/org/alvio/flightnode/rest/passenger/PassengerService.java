package org.alvio.flightnode.rest.passenger;

import org.alvio.flightnode.exception.ConflictException;
import org.alvio.flightnode.mapper.FlightMapper;
import org.alvio.flightnode.mapper.PassengerMapper;
import org.alvio.flightnode.rest.flight.Flight;
import org.alvio.flightnode.rest.flight.FlightRepository;
import org.alvio.flightnode.rest.flight.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightService flightService;

    public List<Passenger> getAllPassengers(boolean showBookings, boolean showAirports, boolean showAircrafts) {
        boolean needFlights = showBookings || showAirports || showAircrafts;
        return needFlights ? passengerRepository.findAllWithFlights() : passengerRepository.findAll();
    }

    public Passenger getPassengerById(Long id, boolean showBookings, boolean showAirports, boolean showAircrafts) {

        boolean needFlights = showBookings || showAirports || showAircrafts;

        Passenger passenger = needFlights
                ? passengerRepository.findWithFlightsById(id)
                : passengerRepository.findById(id).orElse(null);

        if (passenger == null) {
            throw new NoSuchElementException("Passenger with ID " + id + " not found.");
        }

        return passenger;
    }

    public List<Passenger> searchPassengers(String firstName, String lastName) {
        boolean hasFirst = firstName != null && !firstName.isBlank();
        boolean hasLast = lastName != null && !lastName.isBlank();

        if (!hasFirst && !hasLast) {
            return passengerRepository.findAll();
        } else if (hasFirst && !hasLast) {
            return passengerRepository.findByFirstNameContainingIgnoreCase(firstName);
        } else if (!hasFirst && hasLast) {
            return passengerRepository.findByLastNameContainingIgnoreCase(lastName);
        } else {
            return passengerRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        }
    }

    public Passenger addPassenger(Passenger passenger) {
        if (passenger.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new record. Aborted.");
        }
        return passengerRepository.save(passenger);
    }

    public List<Passenger> addPassengers(List<Passenger> passengers) {
        for (Passenger p : passengers) {
            if (p.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new record. Aborted.");
            }
        }
        return passengerRepository.saveAll(passengers);
    }

    public Passenger updatePassenger(Long id, Passenger updatedPassenger) {
        if (updatedPassenger.getId() != null && !updatedPassenger.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Passenger existingPassenger = getPassengerById(id, false, false, false);
        existingPassenger.setFirstName(updatedPassenger.getFirstName());
        existingPassenger.setLastName(updatedPassenger.getLastName());
        existingPassenger.setPhoneNumber(updatedPassenger.getPhoneNumber());
        return passengerRepository.save(existingPassenger);
    }

    public void deletePassenger(Long id) {
        Passenger passenger = getPassengerById(id, false, false, false);
        if (!passenger.getFlights().isEmpty()) {
            throw new ConflictException("Passenger cannot be deleted: has existing bookings.");
        }
        passengerRepository.deleteById(id);
    }

    public PassengerFlightPair bookFlight(Long passengerId, Long flightId) {
        Passenger passenger = getPassengerById(passengerId, false, false, false);
        Flight flight = flightService.getFlightById(flightId);

        if (passenger.getFlights().contains(flight)) {
            throw new ConflictException("Passenger is already booked on this flight.");
        }

        passenger.getFlights().add(flight);
        passengerRepository.save(passenger);

        return new PassengerFlightPair(passenger, flight);
    }

    public PassengerFlightPair removeBooking(Long passengerId, Long flightId) {
        Passenger passenger = getPassengerById(passengerId, true, false, false);
        Flight flight = flightService.getFlightById(flightId);

        if (passenger.getFlights().remove(flight)) {
            passengerRepository.save(passenger);
        }

        return new PassengerFlightPair(passenger, flight);
    }
}
