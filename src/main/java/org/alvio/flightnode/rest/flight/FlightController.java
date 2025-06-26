package org.alvio.flightnode.rest.flight;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.FlightDTO;
import org.alvio.flightnode.mapper.FlightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flight/{id}")
    public ResponseEntity<?> getFlightById(@PathVariable Long id,
                                           @RequestParam(name = "show-passengers", required = false,
                                                         defaultValue = "false") boolean showPassengers)
    {

        try {
            Flight flight = flightService.getFlightById(id);
            FlightDTO flightDto = FlightMapper.toFlightDTO(flight, showPassengers);
            return ResponseEntity.ok(flightDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/flights")
    public ResponseEntity<?> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightDTO> flightsDto = flights.stream()
                .map(FlightMapper::toFlightDTO)
                .toList();
        return ResponseEntity.ok(flightsDto);
    }

    @GetMapping("/flight-search")
    public ResponseEntity<?> searchFlights(
            @RequestParam(name = "starting-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @RequestParam(name = "departure-city") String departureCity,
            @RequestParam(name = "arrival-city") String arrivalCity
    ) {
        try {
            List<Flight> result = flightService.searchFlights(startingDate, departureCity, arrivalCity);
            List<FlightDTO> dtoList = result.stream()
                    .map(FlightMapper::toFlightDTO)
                    .toList();
            return ResponseEntity.ok(dtoList);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }


    @PostMapping("/flight")
    public ResponseEntity<?> addFlight(@Valid @RequestBody Flight flight) {
        try {
            Flight createdFlight = flightService.addFlight(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(FlightMapper.toFlightDTO(createdFlight));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/flights")
    public ResponseEntity<?> addFlights(@Valid @RequestBody List<Flight> flights) {
        try {
            List<Flight> createdFlights = flightService.addFlights(flights);
            List<FlightDTO> createdFlightsDto = createdFlights.stream().map(FlightMapper::toFlightDTO).toList();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFlightsDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage() + " Aborted."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage() + " Aborted."));
        }

    }

    @PutMapping("/flight/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight flight) {
        try {
            Flight updated = flightService.updateFlight(id, flight);
            return ResponseEntity.ok(FlightMapper.toFlightDTO(updated));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/flight/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        try {
            flightService.deleteFlightById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }
}
