package org.alvio.flightnode.rest.passenger;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @EntityGraph(attributePaths = {"flights.departureAirport", "flights.arrivalAirport", "flights.aircraft"})
    @Query("SELECT p FROM Passenger p")
    List<Passenger> findAllWithFlights();

    @EntityGraph(attributePaths = {"flights.departureAirport", "flights.arrivalAirport", "flights.aircraft"})
    @Query("SELECT p FROM Passenger p WHERE p.id = :id")
    Passenger findWithFlightsById(Long id);

    List<Passenger> findByFirstNameContainingIgnoreCase(String firstName);

    List<Passenger> findByLastNameContainingIgnoreCase(String lastName);

    List<Passenger> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumber(
            String firstName,
            String lastName,
            String phoneNumber
    );

}
