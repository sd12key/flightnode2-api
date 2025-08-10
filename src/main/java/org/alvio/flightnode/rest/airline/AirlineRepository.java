package org.alvio.flightnode.rest.airline;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = "aircrafts")
    Optional<Airline> findWithAircraftsById(Long id);

    @EntityGraph(attributePaths = "aircrafts")
    @Query("SELECT a FROM Airline a")
    List<Airline> findAllWithAircrafts();
}
