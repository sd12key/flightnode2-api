package org.alvio.flightnode.rest.airline;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.AirlineDTO;
import org.alvio.flightnode.mapper.AirlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @GetMapping("/airline/{id}")
    public ResponseEntity<?> getAirlineById(@PathVariable Long id,
                                            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false")
                                            boolean showAircrafts) {
        Airline airline = airlineService.getAirlineById(id, showAircrafts);
        AirlineDTO dto = AirlineMapper.toDTO(airline, showAircrafts);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/airlines")
    public ResponseEntity<?> getAllAirlines(
            @RequestParam(name = "show-aircrafts", required = false, defaultValue = "false")
            boolean showAircrafts) {
        List<Airline> airlines = airlineService.getAllAirlines(showAircrafts);
        List<AirlineDTO> dtoList = airlines.stream()
                .map(a -> AirlineMapper.toDTO(a, showAircrafts))
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/airline")
    public ResponseEntity<?> addAirline(@Valid @RequestBody Airline airline) {
        Airline created = airlineService.addAirline(airline);
        AirlineDTO dto = AirlineMapper.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/airlines")
    public ResponseEntity<?> addAirlines(@Valid @RequestBody List<Airline> airlines) {
        List<Airline> created = airlineService.addAirlines(airlines);
        List<AirlineDTO> dtoList = created.stream()
                .map(AirlineMapper::toDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoList);
    }

    @PutMapping("/airline/{id}")
    public ResponseEntity<?> updateAirline(@PathVariable Long id,
                                           @Valid @RequestBody Airline airline) {
        Airline updated = airlineService.updateAirline(id, airline);
        AirlineDTO dto = AirlineMapper.toDTO(updated);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/airline/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirlineById(id);
        return ResponseEntity.noContent().build();
    }
}
