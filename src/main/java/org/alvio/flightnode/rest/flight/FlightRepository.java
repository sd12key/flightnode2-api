package org.alvio.flightnode.rest.flight;

import org.alvio.flightnode.rest.gate.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByDepartureGate(Gate gate);

    boolean existsByArrivalGate(Gate gate);

    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureTime >= :startDate " +
            "AND LOWER(f.departureAirport.city.name) LIKE LOWER(CONCAT('%', :departureCity, '%')) " +
            "AND LOWER(f.arrivalAirport.city.name) LIKE LOWER(CONCAT('%', :arrivalCity, '%'))")
    List<Flight> searchByCitiesAndStartDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity
    );

    @Query("SELECT f FROM Flight f WHERE " +
            "(:startDate IS NULL OR f.departureTime >= :startDate) AND " +
            "(:endDate IS NULL OR f.departureTime <= :endDate) AND " +
            "(:departureCity IS NULL OR LOWER(f.departureAirport.city.name) LIKE LOWER(CONCAT('%', :departureCity, '%'))) AND " +
            "(:arrivalCity IS NULL OR LOWER(f.arrivalAirport.city.name) LIKE LOWER(CONCAT('%', :arrivalCity, '%'))) AND " +
            "(:departureCityState IS NULL OR LOWER(f.departureAirport.city.state) LIKE LOWER(CONCAT('%', :departureCityState, '%'))) AND " +
            "(:arrivalCityState IS NULL OR LOWER(f.arrivalAirport.city.state) LIKE LOWER(CONCAT('%', :arrivalCityState, '%'))) AND " +
            "(:departureAirportName IS NULL OR LOWER(f.departureAirport.name) LIKE LOWER(CONCAT('%', :departureAirportName, '%'))) AND " +
            "(:arrivalAirportName IS NULL OR LOWER(f.arrivalAirport.name) LIKE LOWER(CONCAT('%', :arrivalAirportName, '%'))) AND " +
            "(:departureAirportCode IS NULL OR LOWER(f.departureAirport.code) LIKE LOWER(CONCAT('%', :departureAirportCode, '%'))) AND " +
            "(:arrivalAirportCode IS NULL OR LOWER(f.arrivalAirport.code) LIKE LOWER(CONCAT('%', :arrivalAirportCode, '%'))) AND " +
            "(:airlineName IS NULL OR LOWER(f.aircraft.airline.name) LIKE LOWER(CONCAT('%', :airlineName, '%'))) " +
            "ORDER BY f.departureTime ASC")
    List<Flight> searchFlights(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("departureCityState") String departureCityState,
            @Param("arrivalCityState") String arrivalCityState,
            @Param("departureAirportName") String departureAirportName,
            @Param("arrivalAirportName") String arrivalAirportName,
            @Param("departureAirportCode") String departureAirportCode,
            @Param("arrivalAirportCode") String arrivalAirportCode,
            @Param("airlineName") String airlineName
    );

}
