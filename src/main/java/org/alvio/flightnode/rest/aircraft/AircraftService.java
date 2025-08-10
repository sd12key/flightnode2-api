package org.alvio.flightnode.rest.aircraft;

import org.alvio.flightnode.exception.ConflictException;
import org.alvio.flightnode.rest.airline.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirlineService airlineService;

    public List<Aircraft> getAllAircrafts(boolean showAirports) {
        return showAirports ?
                aircraftRepository.findAllWithAirports() :
                aircraftRepository.findAll();
    }

    public Aircraft getAircraftById(Long id, boolean showAirports) {
        Optional<Aircraft> resultAircraft = showAirports ?
                aircraftRepository.findWithAirportsById(id) :
                aircraftRepository.findById(id);
        if (resultAircraft.isEmpty()) {
            throw new NoSuchElementException("Aircraft with ID " + id + " not found.");
        }
        return resultAircraft.get();
    }

    public Aircraft addAircraft(Aircraft aircraft) {
        if (aircraft.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new record. Aborted.");
        }

        if (aircraft.getAirline() == null || aircraft.getAirline().getId() == null) {
            throw new ConflictException("Valid airline ID must be provided.");
        }
        aircraft.setAirline(airlineService.getAirlineById(aircraft.getAirline().getId()));

        return aircraftRepository.save(aircraft);
    }

    public List<Aircraft> addAircrafts(List<Aircraft> aircrafts) {

        for (Aircraft aircraft : aircrafts) {
            if (aircraft.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new record. Aborted.");
            }
        }

        return aircraftRepository.saveAll(aircrafts);
    }

    public Aircraft updateAircraft(Long id, Aircraft updatedAircraft) {

        if (updatedAircraft.getId() != null && !updatedAircraft.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        if (updatedAircraft.getAirline() == null || updatedAircraft.getAirline().getId() == null) {
            throw new ConflictException("Valid airline ID must be provided.");
        }

        Aircraft existingAircraft = getAircraftById(id, false);

        existingAircraft.setType(updatedAircraft.getType());
        existingAircraft.setCapacity(updatedAircraft.getCapacity());
        existingAircraft.setAirline(
                airlineService.getAirlineById(updatedAircraft.getAirline().getId())
        );

        return aircraftRepository.save(existingAircraft);
    }

    public void deleteAircraftById(Long id) {
        Aircraft deletingAircraft = getAircraftById(id, false);
        if (!deletingAircraft.getFlights().isEmpty()) {
            throw new ConflictException("Aircraft cannot be deleted: it is linked to existing flights.");
        }
        aircraftRepository.deleteById(id);
    }
}
