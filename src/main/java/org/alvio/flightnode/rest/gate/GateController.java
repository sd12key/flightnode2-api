package org.alvio.flightnode.rest.gate;

import jakarta.validation.Valid;
import org.alvio.flightnode.dto.CityDTO;
import org.alvio.flightnode.dto.GateDTO;
import org.alvio.flightnode.mapper.CityMapper;
import org.alvio.flightnode.mapper.GateMapper;
import org.alvio.flightnode.rest.city.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GateController {

    @Autowired
    private GateService gateService;

    @GetMapping("/gate/{id}")
    public ResponseEntity<?> getGateById(@PathVariable Long id) {
        Gate gate = gateService.getGateById(id);
        GateDTO dto = GateMapper.toDTO(gate);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/gates")
    public ResponseEntity<?> getGatesByAirport(@RequestParam(name = "airport-id", required = true) Long airportId) {
        List<Gate> gates = gateService.getGatesByAirportId(airportId);
        List<GateDTO> dtoList = GateMapper.toDTOList(gates);
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/gate")
    public ResponseEntity<?> addGate(@Valid @RequestBody Gate gate) {
        Gate created = gateService.addGate(gate);
        GateDTO dto = GateMapper.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/gates")
    public ResponseEntity<?> addGates(@Valid @RequestBody List<Gate> gates) {
        List<Gate> created = gateService.addGates(gates);
        List<GateDTO> dtoList = GateMapper.toDTOList(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoList);
    }

    @PutMapping("/gate/{id}")
    public ResponseEntity<?> updateGate(@PathVariable Long id, @Valid @RequestBody Gate gate) {
        Gate updated = gateService.updateGate(id, gate);
        return ResponseEntity.ok(GateMapper.toDTO(updated));
    }

    @DeleteMapping("/gate/{id}")
    public ResponseEntity<?> deleteGate(@PathVariable Long id) {
        gateService.deleteGate(id);
        return ResponseEntity.noContent().build();
    }
}
