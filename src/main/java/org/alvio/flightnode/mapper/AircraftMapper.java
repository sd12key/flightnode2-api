package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.AircraftDTO;
import org.alvio.flightnode.dto.AircraftSummaryDTO;
import org.alvio.flightnode.dto.AirportSummaryDTO;
import org.alvio.flightnode.rest.aircraft.Aircraft;
import org.alvio.flightnode.rest.flight.Flight;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AircraftMapper {

    public static AircraftDTO toAircraftDTO(Aircraft aircraft, boolean showAirports) {
        AircraftDTO dto = new AircraftDTO(
                aircraft.getId(),
                aircraft.getType(),
                aircraft.getAirlineName(),
                aircraft.getCapacity()
        );

        if (showAirports && aircraft.getFlights() != null) {
            Set<Long> seenDepartureIds = new HashSet<>();
            Set<Long> seenArrivalIds = new HashSet<>();

            List<AirportSummaryDTO> departureAirports = aircraft.getFlights().stream()
                    .map(Flight::getDepartureAirport)
                    .filter(Objects::nonNull)
                    .map(AirportMapper::toSummary)
                    .filter(a -> seenDepartureIds.add(a.getId())) // deduplicate
                    .toList();

            List<AirportSummaryDTO> arrivalAirports = aircraft.getFlights().stream()
                    .map(Flight::getArrivalAirport)
                    .filter(Objects::nonNull)
                    .map(AirportMapper::toSummary)
                    .filter(a -> seenArrivalIds.add(a.getId())) // deduplicate
                    .toList();

            dto.setDepartureAirports(departureAirports);
            dto.setArrivalAirports(arrivalAirports);
        }

        return dto;
    }


    public static AircraftDTO toAircraftDTO(Aircraft aircraft) {
        return toAircraftDTO(aircraft, false);
    }

    public static AircraftSummaryDTO toSummary(Aircraft aircraft) {
        return new AircraftSummaryDTO(
                aircraft.getId(),
                aircraft.getType(),
                aircraft.getAirlineName()
        );
    }

}
