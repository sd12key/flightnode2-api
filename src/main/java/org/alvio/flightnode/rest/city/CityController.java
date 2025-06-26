package org.alvio.flightnode.rest.city;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.CityDTO;
import org.alvio.flightnode.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/cities")
    public ResponseEntity<?> getAllCities(@RequestParam(name = "show-airports", required = false,
                                                        defaultValue = "false") boolean showAirports) {
        List<City> cities = cityService.getAllCities(showAirports);
        List<CityDTO> citiesDto = cities.stream()
                .map(city -> CityMapper.toCityDTO(city, showAirports))
                .toList();
        return ResponseEntity.ok(citiesDto);
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<?> getCityById(@PathVariable Long id,
                                         @RequestParam(name = "show-airports", required = false,
                                                       defaultValue = "false") boolean showAirports) {
        try {
            City city = cityService.getCityById(id, showAirports);
            CityDTO cityDto = CityMapper.toCityDTO(city, showAirports);
            return ResponseEntity.ok(cityDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/city-search")
    public ResponseEntity<?> searchCities(
            @RequestParam(required = false) String name,
            @RequestParam(name = "show-airports", required = false,
                    defaultValue = "false") boolean showAirports) {
        try {
            List<City> result = cityService.findCities(name, showAirports);
            List<CityDTO> resultDto = result.stream()
                    .map(city -> CityMapper.toCityDTO(city, showAirports))
                    .toList();
            return ResponseEntity.ok(resultDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // POST one city
    @PostMapping("/city")
    public ResponseEntity<?> addCity(@Valid @RequestBody City city) {
        try {
            City createdCity = cityService.addCity(city);
            CityDTO createdCityDto = CityMapper.toCityDTO(createdCity, false);
            // 201
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCityDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    // POST multiple cities, batch fails if any city is a duplicate
    @PostMapping("/cities")
    public ResponseEntity<?> addCities(@Valid @RequestBody List<City> cities) {
        try {
            List<City> createdCities = cityService.addCities(cities);
            List<CityDTO> createdCitiesDto = createdCities.stream()
                    .map(city -> CityMapper.toCityDTO(city, false))
                    .toList();
            // 201
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCitiesDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT update city by ID
    @PutMapping("/city/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @Valid @RequestBody City city) {
        try {
            City updated = cityService.updateCity(id, city);
            CityDTO updatedDto = CityMapper.toCityDTO(updated, false);
            return ResponseEntity.ok(updatedDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCityById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }



}
