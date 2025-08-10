package org.alvio.flightnode.rest.gate;

import org.alvio.flightnode.exception.ConflictException;
import org.alvio.flightnode.rest.airport.Airport;
import org.alvio.flightnode.rest.airport.AirportService;
import org.alvio.flightnode.rest.flight.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GateService {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightRepository flightRepository;

    public Gate getGateById(Long id) {
        return gateRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Gate with id " + id + " not found.")
        );
    }

    public List<Gate> getGatesByAirportId(Long airportId) {
        Airport airport = airportService.getAirportById(airportId);
        return gateRepository.findByAirport(airport);
    }

    public Gate addGate(Gate gate) {

        if (gate.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new gate.");
        }

        Airport airport = airportService.getAirportById(gate.getAirport().getId());
        gate.setAirport(airport);

        Optional<Gate> existing = gateRepository.findByAirportAndName(airport, gate.getName());
        if (existing.isPresent()) {
            throw new ConflictException("Gate '" + gate.getName() + "' already exists for this airport.");
        }

        return gateRepository.save(gate);
    }

    public List<Gate> addGates(List<Gate> gates) {
        for (Gate gate : gates) {
            if (gate.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new gate.");
            }

            Airport airport = airportService.getAirportById(gate.getAirport().getId());
            gate.setAirport(airport);

            Optional<Gate> existing = gateRepository.findByAirportAndName(airport, gate.getName());
            if (existing.isPresent()) {
                throw new ConflictException("Gate '" + gate.getName() +
                        "' already exists for airport ID " + airport.getId() + ".");
            }
        }

        return gateRepository.saveAll(gates);
    }

    public Gate updateGate(Long id, Gate updated) {

        if (updated.getId() != null && !updated.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Gate existing = gateRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Gate with id " + id + " not found.")
        );

        if (!existing.getAirport().getId().equals(updated.getAirport().getId())) {
            throw new ConflictException("Cannot change airport of an existing gate.");
        }

        Optional<Gate> duplicate = gateRepository.findByAirportAndName(existing.getAirport(), updated.getName());
        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            throw new ConflictException("Gate name already exists for this airport.");
        }

        existing.setName(updated.getName().trim().toUpperCase());

        return gateRepository.save(existing);
    }


    public void deleteGate(Long gateId) {
        Gate gate = gateRepository.findById(gateId).orElseThrow(
                () -> new NoSuchElementException("Gate with id " + gateId + " not found.")
        );

        boolean used = flightRepository.existsByDepartureGate(gate) || flightRepository.existsByArrivalGate(gate);
        if (used) {
            throw new ConflictException("Cannot delete gate linked to existing flights.");
        }

        gateRepository.delete(gate);
    }
}
