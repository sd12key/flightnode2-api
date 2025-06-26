package org.alvio.flightnode.rest.airport;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.AirportDTO;
import org.alvio.flightnode.mapper.AirportMapper;
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
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/airports")
    public ResponseEntity<?> getAllAirports() {
        List<Airport> airports = airportService.getAllAirports();
        List<AirportDTO> airportsDto = airports.stream()
                .map(AirportMapper::toAirportDTO)
                .toList();
        return ResponseEntity.ok(airportsDto);

    }

    @GetMapping("/airport/{id}")
    public ResponseEntity<?> getAirportById(@PathVariable Long id) {
        try {
            Airport airport = airportService.getAirportById(id);
            AirportDTO airportDto = AirportMapper.toAirportDTO(airport);
            return ResponseEntity.ok(airportDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/airport-search")
    public ResponseEntity<?> searchAirports(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {

        List<Airport> result = airportService.findAirports(name, code);
        List<AirportDTO> resultDto = result.stream()
                .map(AirportMapper::toAirportDTO)
                .toList();
        return ResponseEntity.ok(resultDto);
    }

    @PostMapping("/airport")
    public ResponseEntity<?> addAirport(@Valid @RequestBody Airport airport) {
        try {
            Airport createdAirport = airportService.addAirport(airport);
            AirportDTO createdAirportDto = AirportMapper.toAirportDTO(createdAirport);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAirportDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(Map.of("city", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/airports")
    public ResponseEntity<?> addAirports(@Valid @RequestBody List<Airport> airports) {
        try {
            List<Airport> createdAirports = airportService.addAirports(airports);
            List<AirportDTO> createdAirportsDto = createdAirports.stream()
                    .map(AirportMapper::toAirportDTO)
                    .toList();
            // 201
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAirportsDto);
        } catch (IllegalArgumentException ex) {
            // 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/airport/{id}")
    public ResponseEntity<?> updateAirport(@PathVariable Long id, @Valid @RequestBody Airport airport) {
        try {
            Airport updated = airportService.updateAirport(id, airport);
            AirportDTO updatedDto = AirportMapper.toAirportDTO(updated);
            return ResponseEntity.ok(updatedDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/airport/{id}")
    public ResponseEntity<?> deleteAirport(@PathVariable Long id) {
        try {
            airportService.deleteAirportById(id);
            // 204 No Content response
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

}
