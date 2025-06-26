package org.alvio.flightnode.rest.city;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndState(String name, String state);

    List<City> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"airports"})
    List<City> findWithAirportsByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"airports"})
    Optional<City> findWithAirportsById(Long id);

    @EntityGraph(attributePaths = {"airports"})
    @Query("SELECT c FROM City c")
    List<City> findAllWithAirports();

}
