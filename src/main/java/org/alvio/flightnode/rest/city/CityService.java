package org.alvio.flightnode.rest.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> getAllCities() {
        return (List<City>) cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("City with id " + id + " not found"));
    }

    public List<City> findCities(String name, String state) {
        if (name != null && state != null) {
            return cityRepository.findByNameContainingIgnoreCaseAndState(name, state);
        } else if (name != null) {
            return cityRepository.findByNameContainingIgnoreCase(name);
        } else if (state != null) {
            return cityRepository.findByState(state);
        } else {
            // alternatively, can return an empty list
            return cityRepository.findAll();
        }
    }

    public City addCity(City city) {
        Optional<City> existing = cityRepository.findByNameAndState(city.getName(), city.getState());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("City with name '" + city.getName() + "' and state '" + city.getState() + "' already exists.");
        }
        return cityRepository.save(city);
    }

    public List<City> addCities(List<City> cities) {
        List<String> duplicates = new ArrayList<>();

        for (City city : cities) {
            Optional<City> existingCity = cityRepository.findByNameAndState(city.getName(), city.getState());
            if (existingCity.isPresent()) {
                duplicates.add(city.getName() + "(" + city.getState() + ")");
            }
        }

        if (!duplicates.isEmpty()) throw new IllegalArgumentException(
                duplicates.size() == 1 ?
                        "City " + duplicates.get(0) + " already exists, aborted." :
                        "Cities " + String.join(", ", duplicates) + " already exist, aborted."
        );

        //        Iterable<City> saved = cityRepository.saveAll(cities);
        //        List<City> result = new ArrayList<>();
        //        saved.forEach(result::add);
        //        return result;
        return cityRepository.saveAll(cities);
    }

    public City updateCity(Long id, City updatedCity) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("City with ID " + id + " not found"));

        Optional<City> duplicateCity = cityRepository.findByNameAndState(updatedCity.getName(), updatedCity.getState());

        if (duplicateCity.isPresent() && duplicateCity.get().getId() != id) {
            throw new IllegalArgumentException("City with name '" + updatedCity.getName() + "' and state '" + updatedCity.getState() + "' already exists.");
        }

        existingCity.setName(updatedCity.getName());
        existingCity.setState(updatedCity.getState());
        existingCity.setPopulation(updatedCity.getPopulation());

        return cityRepository.save(existingCity);
    }

    public void deleteCityById(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new NoSuchElementException("City with ID " + id + " not found.");
        }
        cityRepository.deleteById(id);
    }


}
