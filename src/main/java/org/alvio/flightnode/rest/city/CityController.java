package org.alvio.flightnode.rest.city;

import jakarta.validation.Valid;
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

    // GET all cities
    @GetMapping("/cities")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<?> getCityById(@PathVariable Long id) {
        try {
            City city = cityService.getCityById(id);
            return ResponseEntity.ok(city);
        } catch (NoSuchElementException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/city-search")
    public ResponseEntity<List<City>> searchCities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String state) {

        List<City> result = cityService.findCities(name, state);
        return ResponseEntity.ok(result);
    }

    // POST one city
    @PostMapping("/city")
    public ResponseEntity<?> addCity(@Valid @RequestBody City city) {
        try {
            City created = cityService.addCity(city);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    // POST multiple cities, batch fails
    @PostMapping("/cities")
    public ResponseEntity<?> addCities(@Valid @RequestBody List<City> cities) {
        try {
            List<City> createdCities = cityService.addCities(cities);
            return ResponseEntity.ok(createdCities);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    // PUT update city by ID
    @PutMapping("/city/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @Valid @RequestBody City city) {
        try {
            City updated = cityService.updateCity(id, city);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCityById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (NoSuchElementException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


}
