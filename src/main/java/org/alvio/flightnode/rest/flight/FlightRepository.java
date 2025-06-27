package org.alvio.flightnode.rest.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureTime >= :startDate " +
            "AND LOWER(f.departureAirport.city.name) LIKE LOWER(CONCAT('%', :departureCity, '%')) " +
            "AND LOWER(f.arrivalAirport.city.name) LIKE LOWER(CONCAT('%', :arrivalCity, '%'))")
    List<Flight> searchByCitiesAndStartDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity
    );

}
