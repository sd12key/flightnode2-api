package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.*;
import org.alvio.flightnode.rest.passenger.Passenger;
import org.alvio.flightnode.rest.flight.Flight;

import java.util.*;
import java.util.stream.Stream;

public class PassengerMapper {

    public static PassengerDTO toPassengerDTO(Passenger passenger, boolean showBookings, boolean showAirports, boolean showAircrafts) {
        PassengerDTO dto = new PassengerDTO(
                passenger.getId(),
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getPhoneNumber()
        );

        List<Flight> flights = passenger.getFlights();
        if (flights == null || flights.isEmpty()) {
            return dto;
        }

        if (showBookings) {
            dto.setBookings(flights.stream()
                    .map(FlightMapper::toSummary)
                    .toList());
        }

        if (showAirports) {
            Set<Long> seenAirportIds = new HashSet<>();
            List<AirportSummaryDTO> airports = flights.stream()
                    .flatMap(f -> Stream.of(f.getDepartureAirport(), f.getArrivalAirport()))
                    .filter(Objects::nonNull)
                    .map(AirportMapper::toSummary)
                    .filter(a -> seenAirportIds.add(a.getId()))
                    .sorted(Comparator.comparing(AirportSummaryDTO::getId))
                    .toList();
            dto.setAirports(airports);
        }

        if (showAircrafts) {
            Set<Long> seenAircraftIds = new HashSet<>();
            List<AircraftSummaryDTO> aircrafts = flights.stream()
                    .map(Flight::getAircraft)
                    .filter(Objects::nonNull)
                    .map(AircraftMapper::toSummary)
                    .filter(a -> seenAircraftIds.add(a.getId()))
                    .sorted(Comparator.comparing(AircraftSummaryDTO::getId))
                    .toList();
            dto.setAircrafts(aircrafts);
        }

        return dto;
    }

    public static PassengerDTO toPassengerDTO(Passenger passenger) {
        return toPassengerDTO(passenger, false, false, false);
    }

    public static PassengerSummaryDTO toSummary(Passenger passenger) {
        return new PassengerSummaryDTO(
                passenger.getId(),
                passenger.getFirstName(),
                passenger.getLastName()
        );
    }
}
