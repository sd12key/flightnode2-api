package org.alvio.flightnode.rest.flight;

import org.alvio.flightnode.exception.ConflictException;
import org.alvio.flightnode.rest.aircraft.AircraftService;
import org.alvio.flightnode.rest.airport.AirportService;
import org.alvio.flightnode.rest.gate.Gate;
import org.alvio.flightnode.rest.gate.GateRepository;
import org.alvio.flightnode.rest.gate.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private AirportService airportService;

    @Autowired
    private GateService gateService;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        Optional<Flight> result = flightRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Flight with id " + id + " not found.");
        }
        return result.get();
    }

    public List<Flight> searchFlights(
            LocalDate startDate,
            LocalDate endDate,
            String departureCity,
            String arrivalCity,
            String departureCityState,
            String arrivalCityState,
            String departureAirportName,
            String arrivalAirportName,
            String departureAirportCode,
            String arrivalAirportCode,
            String airlineName) {

        // Convert dates to LocalDateTime
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        return flightRepository.searchFlights(
                startDateTime,
                endDateTime,
                departureCity,
                arrivalCity,
                departureCityState,
                arrivalCityState,
                departureAirportName,
                arrivalAirportName,
                departureAirportCode,
                arrivalAirportCode,
                airlineName
        );
    }

    private void validateNewFlight(Flight flight) {
        if (flight.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new flight.");
        }
        validateAndSetAssociations(flight);
    }

    private void validateAndSetAssociations(Flight flight) {
        // Validate presence of required IDs
        if (flight.getAircraft() == null || flight.getAircraft().getId() == null) {
            throw new ConflictException("Valid aircraft ID must be provided.");
        }
        if (flight.getDepartureAirport() == null || flight.getDepartureAirport().getId() == null) {
            throw new ConflictException("Valid departure airport ID must be provided.");
        }
        if (flight.getArrivalAirport() == null || flight.getArrivalAirport().getId() == null) {
            throw new ConflictException("Valid arrival airport ID must be provided.");
        }
        if (flight.getDepartureGate() == null || flight.getDepartureGate().getId() == null) {
            throw new ConflictException("Valid departure gate ID must be provided.");
        }
        if (flight.getArrivalGate() == null || flight.getArrivalGate().getId() == null) {
            throw new ConflictException("Valid arrival gate ID must be provided.");
        }

        // Load and validate related objects
        Gate depGate = gateService.getGateById(flight.getDepartureGate().getId());
        Gate arrGate = gateService.getGateById(flight.getArrivalGate().getId());

        if (!depGate.getAirport().getId().equals(flight.getDepartureAirport().getId())) {
            throw new ConflictException("Departure gate does not belong to the selected departure airport.");
        }

        if (!arrGate.getAirport().getId().equals(flight.getArrivalAirport().getId())) {
            throw new ConflictException("Arrival gate does not belong to the selected arrival airport.");
        }

        // Set resolved and validated entities
        flight.setAircraft(aircraftService.getAircraftById(flight.getAircraft().getId(), false));
        flight.setDepartureAirport(airportService.getAirportById(flight.getDepartureAirport().getId()));
        flight.setArrivalAirport(airportService.getAirportById(flight.getArrivalAirport().getId()));
        flight.setDepartureGate(depGate);
        flight.setArrivalGate(arrGate);
    }

    public Flight addFlight(Flight flight) {
        validateNewFlight(flight);
        return flightRepository.save(flight);
    }

    public List<Flight> addFlights(List<Flight> flights) {
        for (Flight flight : flights) { validateNewFlight(flight); }
        return flightRepository.saveAll(flights);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        if (updatedFlight.getId() != null && !updatedFlight.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Flight existing = getFlightById(id);

        if (!existing.getPassengers().isEmpty()) {
            throw new ConflictException("Cannot modify a flight that has existing bookings.");
        }

        validateAndSetAssociations(updatedFlight);

        existing.setFlightNumber(updatedFlight.getFlightNumber());
        existing.setDepartureTime(updatedFlight.getDepartureTime());
        existing.setArrivalTime(updatedFlight.getArrivalTime());
        existing.setAircraft(updatedFlight.getAircraft());
        existing.setDepartureAirport(updatedFlight.getDepartureAirport());
        existing.setArrivalAirport(updatedFlight.getArrivalAirport());

        return flightRepository.save(existing);
    }

    public void deleteFlightById(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NoSuchElementException("Flight with id " + id + " not found.");
        }
        flightRepository.deleteById(id);
    }
}
