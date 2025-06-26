package org.alvio.flightnode.rest.aircraft;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.AircraftDTO;
import org.alvio.flightnode.mapper.AircraftMapper;
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
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @GetMapping("/aircrafts")
    public ResponseEntity<?> getAllAircrafts(@RequestParam(name = "show-airports", required = false,
                                                           defaultValue = "false") boolean showAirports) {
        List<Aircraft> aircrafts = aircraftService.getAllAircrafts(showAirports);
        List<AircraftDTO> aircraftsDto = aircrafts.stream()
                .map(AircraftMapper::toAircraftDTO)
                .toList();
        return ResponseEntity.ok(aircraftsDto);
    }

    @GetMapping("/aircraft/{id}")
    public ResponseEntity<?> getAircraftById(@PathVariable Long id,
                                             @RequestParam(name = "show-airports", required = false,
                                                           defaultValue = "false") boolean showAirports) {
        try {
            Aircraft aircraft = aircraftService.getAircraftById(id, showAirports);
            AircraftDTO aircraftDto = AircraftMapper.toAircraftDTO(aircraft, showAirports);
            return ResponseEntity.ok(aircraftDto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/aircraft")
    public ResponseEntity<?> addAircraft(@Valid @RequestBody Aircraft aircraft) {
        Aircraft created = aircraftService.addAircraft(aircraft);
        AircraftDTO createdDto = AircraftMapper.toAircraftDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @PostMapping("/aircrafts")
    public ResponseEntity<?> addAircrafts(@Valid @RequestBody List<Aircraft> aircrafts) {
        List<Aircraft> created = aircraftService.addAircrafts(aircrafts);
        List<AircraftDTO> createdDtos = created.stream().map(AircraftMapper::toAircraftDTO).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDtos);
    }

    @PutMapping("/aircraft/{id}")
    public ResponseEntity<?> updateAircraft(@PathVariable Long id, @Valid @RequestBody Aircraft aircraft) {
        try {
            Aircraft updated = aircraftService.updateAircraft(id, aircraft);
            return ResponseEntity.ok(AircraftMapper.toAircraftDTO(updated));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/aircraft/{id}")
    public ResponseEntity<?> deleteAircraft(@PathVariable Long id) {
        try {
            aircraftService.deleteAircraftById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }
}
