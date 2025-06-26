package org.alvio.flightnode.rest.aircraft;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    @EntityGraph(attributePaths = {"flights.departureAirport", "flights.arrivalAirport"})
    @Query("SELECT a FROM Aircraft a")
    List<Aircraft> findAllWithAirports();

    @EntityGraph(attributePaths = {"flights.departureAirport", "flights.arrivalAirport"})
    @Query("SELECT a FROM Aircraft a WHERE a.id = :id")
    Optional<Aircraft> findWithAirportsById(Long id);

}
