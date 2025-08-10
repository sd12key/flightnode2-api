package org.alvio.flightnode.rest.airline;

import org.alvio.flightnode.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    public Airline getAirlineById(Long id, boolean showAircrafts) {
        Optional<Airline> result = showAircrafts ?
                airlineRepository.findWithAircraftsById(id) :
                airlineRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Airline with ID " + id + " not found.");
        }
        return result.get();
    }

    public List<Airline> getAllAirlines(boolean showAircrafts) {
        return showAircrafts ?
                airlineRepository.findAllWithAircrafts() :
                airlineRepository.findAll();
    }

    public Airline getAirlineById(Long id) {
        return getAirlineById(id, false);
    }

    public Airline addAirline(Airline airline) {
        if (airline.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating new airline record.");
        }

        Optional<Airline> existing = airlineRepository.findByNameIgnoreCase(airline.getName());
        if (existing.isPresent()) {
            throw new ConflictException("Airline '" + airline.getName() + "' already exists.");
        }

        return airlineRepository.save(airline);
    }

    public List<Airline> addAirlines(List<Airline> airlines) {
        List<String> duplicates = new ArrayList<>();

        for (Airline airline : airlines) {
            if (airline.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating new airline record.");
            }

            Optional<Airline> existing = airlineRepository.findByNameIgnoreCase(airline.getName());
            if (existing.isPresent()) {
                duplicates.add(airline.getName());
            }
        }

        if (!duplicates.isEmpty()) {
            throw new ConflictException(
                    duplicates.size() == 1 ?
                            "Airline '" + duplicates.get(0) + "' already exists, aborted." :
                            "Airlines " + String.join(", ", duplicates) + " already exist, aborted."
            );
        }

        return airlineRepository.saveAll(airlines);
    }

    public Airline updateAirline(Long id, Airline updatedAirline) {
        if (updatedAirline.getId() != null && !updatedAirline.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Airline existingAirline = getAirlineById(id);

        Optional<Airline> duplicate = airlineRepository.findByNameIgnoreCase(updatedAirline.getName());
        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            throw new ConflictException("Airline '" + updatedAirline.getName() + "' already exists.");
        }

        existingAirline.setName(updatedAirline.getName());

        return airlineRepository.save(existingAirline);
    }

    public void deleteAirlineById(Long id) {
        Airline airline = getAirlineById(id);
        if (!airline.getAircrafts().isEmpty()) {
            throw new ConflictException("Cannot delete airline: it has associated aircraft.");
        }
        airlineRepository.deleteById(id);
    }
}
