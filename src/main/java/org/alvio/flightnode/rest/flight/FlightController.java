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
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flight/{id}")
    public ResponseEntity<?> getFlightById(@PathVariable Long id,
                                           @RequestParam(name = "show-passengers", required = false,
                                                         defaultValue = "false") boolean showPassengers) {
        Flight flight = flightService.getFlightById(id);
        FlightDTO flightDto = FlightMapper.toFlightDTO(flight, showPassengers);
        return ResponseEntity.ok(flightDto);
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
            @RequestParam(name = "start-date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(name = "end-date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(name = "departure-city", required = false) String departureCity,
            @RequestParam(name = "arrival-city", required = false) String arrivalCity,
            @RequestParam(name = "departure-airport-name", required = false) String departureAirportName,
            @RequestParam(name = "arrival-airport-name", required = false) String arrivalAirportName,
            @RequestParam(name = "departure-airport-code", required = false) String departureAirportCode,
            @RequestParam(name = "arrival-airport-code", required = false) String arrivalAirportCode,
            @RequestParam(name = "airline-name", required = false) String airlineName) {

        if (Stream.of(startDate, endDate, departureCity, arrivalCity,
                        departureAirportName, arrivalAirportName,
                        departureAirportCode, arrivalAirportCode, airlineName)
                .allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest()
                    .body("At least one search parameter is required");
        }

        List<Flight> result = flightService.searchFlights(
                startDate,
                endDate,
                departureCity,  // No case conversion needed
                arrivalCity,
                departureAirportName,
                arrivalAirportName,
                departureAirportCode,  // Repository will uppercase
                arrivalAirportCode,
                airlineName
        );

        List<FlightDTO> dtoList = result.stream()
                .map(FlightMapper::toFlightDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/flight")
    public ResponseEntity<?> addFlight(@Valid @RequestBody Flight flight) {
        Flight createdFlight = flightService.addFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(FlightMapper.toFlightDTO(createdFlight));
    }

    @PostMapping("/flights")
    public ResponseEntity<?> addFlights(@Valid @RequestBody List<Flight> flights) {
        List<Flight> createdFlights = flightService.addFlights(flights);
        List<FlightDTO> createdFlightsDto = createdFlights.stream().map(FlightMapper::toFlightDTO).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlightsDto);
    }

    @PutMapping("/flight/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight flight) {
        Flight updated = flightService.updateFlight(id, flight);
        return ResponseEntity.ok(FlightMapper.toFlightDTO(updated));
    }

    @DeleteMapping("/flight/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return ResponseEntity.noContent().build();
    }
}
