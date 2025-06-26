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

    public List<City> getAllCities(boolean showAirports) {
        return showAirports ?
                cityRepository.findAllWithAirports() :
                cityRepository.findAll();
    }

    public City getCityById(Long id, boolean showAirports) {
        Optional<City> cityResult = showAirports ?
                cityRepository.findWithAirportsById(id) :
                cityRepository.findById(id);
        if (cityResult.isEmpty()) {
            throw new NoSuchElementException("City with id " + id + " not found");
        }
        return cityResult.get();
    }

    public City getCityById(Long id) {
        return getCityById(id, false);
    }

    public List<City> findCities(String name, boolean showAirports) {
        if (name == null) {
            return showAirports ?
                    cityRepository.findAllWithAirports() :
                    cityRepository.findAll();
        }
        return showAirports ?
                cityRepository.findWithAirportsByNameContainingIgnoreCase(name) :
                cityRepository.findByNameContainingIgnoreCase(name);
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

        return cityRepository.saveAll(cities);
    }

    public City updateCity(Long id, City updatedCity) {
        City existingCity = getCityById(id);

        Optional<City> duplicateCity = cityRepository.findByNameAndState(updatedCity.getName(), updatedCity.getState());

        if (duplicateCity.isPresent() && !duplicateCity.get().getId().equals(id)) {
            throw new IllegalArgumentException("City with name '" + updatedCity.getName() + "' and state '" + updatedCity.getState() + "' already exists.");
        }

        existingCity.setName(updatedCity.getName());
        existingCity.setState(updatedCity.getState());
        existingCity.setPopulation(updatedCity.getPopulation());

        return cityRepository.save(existingCity);
    }

    public void deleteCityById(Long id) {
        City city = getCityById(id);

        if (!city.getAirports().isEmpty()) {
            throw new IllegalStateException("City cannot be deleted: it is linked to existing airports.");
        }

        cityRepository.deleteById(id);
    }





}
