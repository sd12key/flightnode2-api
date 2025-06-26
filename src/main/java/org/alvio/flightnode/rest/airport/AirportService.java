package org.alvio.flightnode.rest.airport;

import org.alvio.flightnode.rest.city.City;
import org.alvio.flightnode.rest.city.CityService;
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
        Optional <Airport> existing = airportRepository.findByCode(airport.getCode());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Airport '" + airport.getName() +
                    "' (" + airport.getCode() + ") already exists.");
        }

        Long cityId = airport.getCity() != null ? airport.getCity().getId() : null;
        if (cityId == null) {
            throw new IllegalArgumentException("City ID must be provided.");
        }

        City fullCity = cityService.getCityById(cityId);
        airport.setCity(fullCity);
        return airportRepository.save(airport);
    }

    public List<Airport> addAirports(List<Airport> airports) {
        List<String> duplicates = new ArrayList<>();
        for (Airport airport : airports) {
            Optional<Airport> existing = airportRepository.findByCode(airport.getCode());
            if (existing.isPresent()) {
                duplicates.add(airport.getName() + " (" + airport.getCode() + ")");
            }
        }

        if (!duplicates.isEmpty()) throw new IllegalArgumentException(
                duplicates.size() == 1 ?
                        "Airport " + duplicates.get(0) + " already exists, aborted." :
                        "Airports " + String.join(", ", duplicates) + " already exist, aborted."
        );

        return airportRepository.saveAll(airports);
    }

    public Airport updateAirport(Long id, Airport updatedAirport) {
        Airport existingAirport = getAirportById(id);

        Optional<Airport> duplicateAirport = airportRepository.findByCode(updatedAirport.getCode());
        if (duplicateAirport.isPresent() && !duplicateAirport.get().getId().equals(id)) {
            throw new IllegalArgumentException("Airport code '" + updatedAirport.getCode() + "' already exists.");
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
            throw new IllegalStateException("Airport cannot be deleted: it is linked to existing flights.");
        }

        airportRepository.deleteById(id);
    }

}
