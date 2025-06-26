package org.alvio.flightnode.rest.passenger;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.BookingRequestDTO;
import org.alvio.flightnode.dto.PassengerDTO;
import org.alvio.flightnode.mapper.FlightMapper;
import org.alvio.flightnode.mapper.PassengerMapper;
import org.alvio.flightnode.rest.flight.Flight;
import org.alvio.flightnode.rest.flight.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private FlightService flightService;

    @GetMapping("/passengers")
    public ResponseEntity<?> getAllPassengers(
            @RequestParam(name = "show-bookings", required = false, defaultValue = "false") boolean showBookings,
            @RequestParam(name = "show-airports", required = false, defaultValue = "false") boolean showAirports,
            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false") boolean showAircrafts
    ) {
        List<PassengerDTO> PassengersDto = passengerService.getAllPassengers(showBookings, showAirports, showAircrafts).stream()
                .map(p -> PassengerMapper.toPassengerDTO(p, showBookings, showAirports, showAircrafts))
                .toList();
        return ResponseEntity.ok(PassengersDto);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<?> getPassengerById(
            @PathVariable Long id,
            @RequestParam(name = "show-bookings", required = false, defaultValue = "false") boolean showBookings,
            @RequestParam(name = "show-airports", required = false, defaultValue = "false") boolean showAirports,
            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false") boolean showAircrafts
    ) {
        try {
            Passenger p = passengerService.getPassengerById(id, showBookings, showAirports, showAircrafts);
            return ResponseEntity.ok(PassengerMapper.toPassengerDTO(p, showBookings, showAirports, showAircrafts));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/passenger-search")
    public ResponseEntity<?> searchPassengers(
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName
    ) {
        List<PassengerDTO> PassengersDto = passengerService.searchPassengers(firstName, lastName).stream()
                .map(PassengerMapper::toPassengerDTO)
                .toList();
        return ResponseEntity.ok(PassengersDto);
    }

    @PostMapping("/passenger")
    public ResponseEntity<?> addPassenger(@Valid @RequestBody Passenger passenger) {
        try {
            Passenger created = passengerService.addPassenger(passenger);
            return ResponseEntity.status(HttpStatus.CREATED).body(PassengerMapper.toPassengerDTO(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/passengers")
    public ResponseEntity<?> addPassengers(@Valid @RequestBody List<Passenger> passengers) {
        try {
            List<Passenger> createdPassenger = passengerService.addPassengers(passengers);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    createdPassenger.stream().map(PassengerMapper::toPassengerDTO).toList()
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/passenger/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable Long id, @Valid @RequestBody Passenger passenger) {
        try {
            Passenger updatedPassenger = passengerService.updatePassenger(id, passenger);
            return ResponseEntity.ok(PassengerMapper.toPassengerDTO(updatedPassenger));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/passenger/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable Long id) {
        try {
            passengerService.deletePassenger(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@Valid @RequestBody BookingRequestDTO request) {
        try {
            Passenger passenger = passengerService.getPassengerById(request.getPassengerId(), false, false, false);
            Flight flight = flightService.getFlightById(request.getFlightId());

            passengerService.bookFlight(passenger.getId(), flight.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Flight booked.",
                    "passenger", PassengerMapper.toSummary(passenger),
                    "flight", FlightMapper.toSummary(flight)
            ));

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Passenger is already booked on this flight."));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", ex.getClass().getSimpleName() + ": " + ex.getMessage()
            ));
        }
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> bookFlights(@Valid @RequestBody List<BookingRequestDTO> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request must include at least one booking."));
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (BookingRequestDTO dto : bookings) {
            try {
                Passenger passenger = passengerService.getPassengerById(dto.getPassengerId(), false, false, false);
                Flight flight = flightService.getFlightById(dto.getFlightId());

                passengerService.bookFlight(passenger.getId(), flight.getId());

                results.add(Map.of(
                        "message", "Flight booked.",
                        "passenger", PassengerMapper.toSummary(passenger),
                        "flight", FlightMapper.toSummary(flight)
                ));
            } catch (Exception ex) {
                results.add(Map.of(
                        "error", ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                        "passengerId", dto.getPassengerId(),
                        "flightId", dto.getFlightId()
                ));
            }
        }

        return ResponseEntity.ok(results);
    }


    @DeleteMapping("/book")
    public ResponseEntity<?> removeBooking(@Valid @RequestBody BookingRequestDTO req) {
        try {
            Passenger passenger = passengerService.getPassengerById(req.getPassengerId(), false, false, false);
            Flight flight = flightService.getFlightById(req.getFlightId());

            passengerService.removeBooking(passenger.getId(), flight.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Booking removed.",
                    "passenger", PassengerMapper.toSummary(passenger),
                    "flight", FlightMapper.toSummary(flight)
            ));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", ex.getClass().getSimpleName() + ": " + ex.getMessage()
            ));
        }
    }

}
