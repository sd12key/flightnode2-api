package org.alvio.flightnode.rest.gate;

import org.alvio.flightnode.rest.airport.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GateRepository extends JpaRepository<Gate, Long> {

    Optional<Gate> findByAirportAndName(Airport airport, String name);

    List<Gate> findByAirport(Airport airport);

}
