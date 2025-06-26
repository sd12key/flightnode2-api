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
            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false") boolean showAircrafts ) {

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
            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false") boolean showAircrafts ) {

        Passenger p = passengerService.getPassengerById(id, showBookings, showAirports, showAircrafts);
        return ResponseEntity.ok(PassengerMapper.toPassengerDTO(p, showBookings, showAirports, showAircrafts));
    }

    @GetMapping("/passenger-search")
    public ResponseEntity<?> searchPassengers(
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName ) {

        List<PassengerDTO> PassengersDto = passengerService.searchPassengers(firstName, lastName).stream()
                .map(PassengerMapper::toPassengerDTO)
                .toList();
        return ResponseEntity.ok(PassengersDto);
    }

    @PostMapping("/passenger")
    public ResponseEntity<?> addPassenger(@Valid @RequestBody Passenger passenger) {

        Passenger created = passengerService.addPassenger(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(PassengerMapper.toPassengerDTO(created));
    }

    @PostMapping("/passengers")
    public ResponseEntity<?> addPassengers(@Valid @RequestBody List<Passenger> passengers) {

        List<Passenger> createdPassenger = passengerService.addPassengers(passengers);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                createdPassenger.stream().map(PassengerMapper::toPassengerDTO).toList()
        );
    }

    @PutMapping("/passenger/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable Long id, @Valid @RequestBody Passenger passenger) {
        Passenger updatedPassenger = passengerService.updatePassenger(id, passenger);
        return ResponseEntity.ok(PassengerMapper.toPassengerDTO(updatedPassenger));
    }

    @DeleteMapping("/passenger/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable Long id) {

        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@Valid @RequestBody BookingRequestDTO request) {
        PassengerFlightPair result = passengerService.bookFlight(
                request.getPassengerId(), request.getFlightId()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Flight booked.",
                "passenger", PassengerMapper.toSummary(result.passenger()),
                "flight", FlightMapper.toSummary(result.flight())
        ));
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> bookFlights(@Valid @RequestBody List<BookingRequestDTO> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request must include at least one booking."));
        }

        List<Map<String, Object>> results = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (BookingRequestDTO dto : bookings) {
            try {
                PassengerFlightPair result = passengerService.bookFlight(dto.getPassengerId(), dto.getFlightId());

                results.add(Map.of(
                        "message", "Flight booked.",
                        "passenger", PassengerMapper.toSummary(result.passenger()),
                        "flight", FlightMapper.toSummary(result.flight())
                ));
                successCount++;
            } catch (Exception ex) {
                results.add(Map.of(
                        "error", ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                        "passengerId", dto.getPassengerId(),
                        "flightId", dto.getFlightId()
                ));
                failureCount++;
            }
        }

        Map<String, Object> response = Map.of(
                "successCount", successCount,
                "failureCount", failureCount,
                "results", results
        );

        if (successCount == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (failureCount > 0) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        } else {
            return ResponseEntity.ok(response);
        }

    }

    @DeleteMapping("/book")
    public ResponseEntity<?> removeBooking(@Valid @RequestBody BookingRequestDTO req) {
        PassengerFlightPair result = passengerService.removeBooking(req.getPassengerId(), req.getFlightId());

        return ResponseEntity.ok(Map.of(
                "message", "Booking removed.",
                "passenger", PassengerMapper.toSummary(result.passenger()),
                "flight", FlightMapper.toSummary(result.flight())
        ));
    }
}


