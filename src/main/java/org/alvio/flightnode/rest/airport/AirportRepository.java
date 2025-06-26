package org.alvio.flightnode.rest.airport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);
    List<Airport> findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(String name, String code);
    List<Airport> findByNameContainingIgnoreCase(String name);
    List<Airport> findByCodeContainingIgnoreCase(String name);

}
