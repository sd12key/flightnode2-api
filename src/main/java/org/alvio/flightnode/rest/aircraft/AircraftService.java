package org.alvio.flightnode.rest.aircraft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    public List<Aircraft> getAllAircrafts(boolean showAirports) {
        return showAirports ?
                aircraftRepository.findAll() :
                aircraftRepository.findAll();
    }

    public Aircraft getAircraftById(Long id, boolean showAirports) {
        Optional<Aircraft> resultAircraft = showAirports ?
                aircraftRepository.findById(id) :
                aircraftRepository.findById(id);
        if (resultAircraft.isEmpty()) {
            throw new NoSuchElementException("Aircraft with id " + id + " not found.");
        }
        return resultAircraft.get();
    }

//    public Aircraft getAircraftById(Long id) {
//        return getAircraftById(id, false);
//    }

    public Aircraft addAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    public List<Aircraft> addAircrafts(List<Aircraft> aircrafts) {
        return aircraftRepository.saveAll(aircrafts);
    }

    public Aircraft updateAircraft(Long id, Aircraft updatedAircraft) {
        Aircraft existingAircraft = getAircraftById(id, false);

        existingAircraft.setType(updatedAircraft.getType());
        existingAircraft.setAirlineName(updatedAircraft.getAirlineName());
        existingAircraft.setCapacity(updatedAircraft.getCapacity());

        return aircraftRepository.save(existingAircraft);
    }

    public void deleteAircraftById(Long id) {
        Aircraft deletingAircraft = getAircraftById(id, true);
        // future: check if aircraft.getFlights().isEmpty()
        aircraftRepository.deleteById(id);
    }
}
