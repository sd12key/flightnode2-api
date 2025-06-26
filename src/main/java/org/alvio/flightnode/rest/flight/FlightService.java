package org.alvio.flightnode.rest.flight;

import org.alvio.flightnode.rest.aircraft.Aircraft;
import org.alvio.flightnode.rest.aircraft.AircraftRepository;
import org.alvio.flightnode.rest.aircraft.AircraftService;
import org.alvio.flightnode.rest.airport.Airport;
import org.alvio.flightnode.rest.airport.AirportRepository;
import org.alvio.flightnode.rest.airport.AirportService;
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

    public List<Flight> searchFlights(LocalDate startDate, String departureCity, String arrivalCity) {
        if (departureCity == null || arrivalCity == null || startDate == null) {
            throw new IllegalArgumentException("All parameters are required.");
        }

        LocalDateTime from = startDate.atStartOfDay();
        return flightRepository.searchByCitiesAndStartDate(from, departureCity, arrivalCity);
    }

    // helper method to validate and create a full flight object
    private void validateAndCreateFullFlight(Flight flight) {
        if (flight.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new flight.");
        }

        if (flight.getAircraft() == null || flight.getAircraft().getId() == null) {
            throw new IllegalArgumentException("Aircraft ID must be provided.");
        }
        if (flight.getDepartureAirport() == null || flight.getDepartureAirport().getId() == null) {
            throw new IllegalArgumentException("Departure airport ID must be provided.");
        }
        if (flight.getArrivalAirport() == null || flight.getArrivalAirport().getId() == null) {
            throw new IllegalArgumentException("Arrival airport ID must be provided.");
        }

        Aircraft aircraft = aircraftService.getAircraftById(flight.getAircraft().getId(), false);
        Airport departureAirport = airportService.getAirportById(flight.getDepartureAirport().getId());
        Airport arrivalAirport = airportService.getAirportById(flight.getArrivalAirport().getId());

        flight.setAircraft(aircraft);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
    }

    public Flight addFlight(Flight flight) {
        validateAndCreateFullFlight(flight);
        return flightRepository.save(flight);
    }

    public List<Flight> addFlights(List<Flight> flights) {
        for (Flight flight : flights) { validateAndCreateFullFlight(flight); }
        return flightRepository.saveAll(flights);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        if (updatedFlight.getId() != null && !updatedFlight.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }
        getFlightById(id);  // if not found, this will throw an exception
        validateAndCreateFullFlight(updatedFlight);
        return flightRepository.save(updatedFlight);
    }

    public void deleteFlightById(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NoSuchElementException("Flight with id " + id + " not found.");
        }
        flightRepository.deleteById(id);
    }
}
