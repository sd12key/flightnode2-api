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
                .map(aircraft -> AircraftMapper.toAircraftDTO(aircraft, showAirports)).toList();
        return ResponseEntity.ok(aircraftsDto);
    }

    @GetMapping("/aircraft/{id}")
    public ResponseEntity<?> getAircraftById(@PathVariable Long id,
                                             @RequestParam(name = "show-airports", required = false,
                                                           defaultValue = "false") boolean showAirports) {
        Aircraft aircraft = aircraftService.getAircraftById(id, showAirports);
        AircraftDTO aircraftDto = AircraftMapper.toAircraftDTO(aircraft, showAirports);
        return ResponseEntity.ok(aircraftDto);
    }

    @PostMapping("/aircraft")
    public ResponseEntity<?> addAircraft(@Valid @RequestBody Aircraft aircraft) {
        Aircraft createdAircraft = aircraftService.addAircraft(aircraft);
        AircraftDTO createdAircraftDto = AircraftMapper.toAircraftDTO(createdAircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAircraftDto);
    }

    @PostMapping("/aircrafts")
    public ResponseEntity<?> addAircrafts(@Valid @RequestBody List<Aircraft> aircrafts) {
        List<Aircraft> createdAircrafts = aircraftService.addAircrafts(aircrafts);
        List<AircraftDTO> createdAircraftsDto = createdAircrafts.stream().map(AircraftMapper::toAircraftDTO).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAircraftsDto);
    }

    @PutMapping("/aircraft/{id}")
    public ResponseEntity<?> updateAircraft(@PathVariable Long id, @Valid @RequestBody Aircraft aircraft) {

        Aircraft updatedAircraft = aircraftService.updateAircraft(id, aircraft);
        return ResponseEntity.ok(AircraftMapper.toAircraftDTO(updatedAircraft));
    }

    @DeleteMapping("/aircraft/{id}")
    public ResponseEntity<?> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraftById(id);
        return ResponseEntity.noContent().build();

    }
}
