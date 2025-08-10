package org.alvio.flightnode.rest.airport;

import org.alvio.flightnode.exception.ConflictException;
import org.alvio.flightnode.rest.city.City;
import org.alvio.flightnode.rest.city.CityService;
import org.alvio.flightnode.rest.gate.Gate;
import org.alvio.flightnode.rest.gate.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private CityService cityService;

    public List<Airport> getAllAirports() {
        return (airportRepository.findAll());
    }

    public Airport getAirportById(Long id) {
        Optional<Airport> airportResult = airportRepository.findById(id);
        if (airportResult.isEmpty()) {
            throw new NoSuchElementException("Airport with id " + id + " not found.");
        }
        return airportResult.get();
    }

    public List<Airport> findAirports(String name, String code) {
        if (name != null && code != null) {
            return airportRepository.findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(name, code);
        } else if (name != null) {
            return airportRepository.findByNameContainingIgnoreCase(name);
        } else if (code != null) {
            return airportRepository.findByCodeContainingIgnoreCase(code);
        } else {
            return airportRepository.findAll();
        }
    }


    public Airport addAirport(Airport airport) {
        if (airport.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new record.");
        }

        Optional <Airport> existing = airportRepository.findByCode(airport.getCode());
        if (existing.isPresent()) {
            throw new ConflictException("Airport '" + airport.getName() +
                    "' (" + airport.getCode() + ") already exists.");
        }

        Long cityId = airport.getCity() != null ? airport.getCity().getId() : null;
        if (cityId == null) {
            throw new IllegalArgumentException("City ID must be provided.");
        }

        City fullCity = cityService.getCityById(cityId);
        airport.setCity(fullCity);

        Airport savedAirport = airportRepository.save(airport);

        Gate defaultGate = new Gate();
        defaultGate.setName("TBD");
        defaultGate.setAirport(savedAirport);
        gateRepository.save(defaultGate);

        return savedAirport;
    }

    public List<Airport> addAirports(List<Airport> airports) {
        List<String> duplicates = new ArrayList<>();
        for (Airport airport : airports) {
            if (airport.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new record.");
            }

            Optional<Airport> existing = airportRepository.findByCode(airport.getCode());
            if (existing.isPresent()) {
                duplicates.add(airport.getName() + " (" + airport.getCode() + ")");
            }

            Long cityId = airport.getCity() != null ? airport.getCity().getId() : null;
            if (cityId == null) {
                throw new IllegalArgumentException("City ID must be provided for airport: " + airport.getName());
            }

            City fullCity = cityService.getCityById(cityId);
            airport.setCity(fullCity);

        }

        if (!duplicates.isEmpty()) throw new ConflictException(
                duplicates.size() == 1 ?
                        "Airport " + duplicates.get(0) + " already exists, aborted." :
                        "Airports " + String.join(", ", duplicates) + " already exist, aborted."
        );

        List<Airport> savedAirports = airportRepository.saveAll(airports);

        for (Airport airport : savedAirports) {
            Gate defaultGate = new Gate();
            defaultGate.setName("TBD");
            defaultGate.setAirport(airport);
            gateRepository.save(defaultGate);
        }

        return savedAirports;
    }

    public Airport updateAirport(Long id, Airport updatedAirport) {

        if (updatedAirport.getId() != null && !updatedAirport.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Airport existingAirport = getAirportById(id);

        Optional<Airport> duplicateAirport = airportRepository.findByCode(updatedAirport.getCode());
        if (duplicateAirport.isPresent() && !duplicateAirport.get().getId().equals(id)) {
            throw new ConflictException("Airport code '" + updatedAirport.getCode() + "' already exists.");
        }

        City newCity = cityService.getCityById(updatedAirport.getCity().getId());

        existingAirport.setName(updatedAirport.getName());
        existingAirport.setCode(updatedAirport.getCode());
        existingAirport.setCity(newCity);

        return airportRepository.save(existingAirport);
    }

    public void deleteAirportById(Long id) {
        Airport airport = getAirportById(id);

        if (!airport.getDepartureFlights().isEmpty() || !airport.getArrivalFlights().isEmpty()) {
            throw new ConflictException("Airport cannot be deleted: it is linked to existing flights.");
        }

        airportRepository.deleteById(id);
    }

}
