package org.alvio.flightnode.rest.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndState(String name, String state);
    List<City> findByNameContainingIgnoreCase(String name);
    List<City> findByNameContainingIgnoreCaseAndState(String name, String state);
    List<City> findByState(String state);
}
