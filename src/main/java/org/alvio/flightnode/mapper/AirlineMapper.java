package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.AircraftSummaryDTO;
import org.alvio.flightnode.dto.AirlineDTO;
import org.alvio.flightnode.dto.AirlineSummaryDTO;
import org.alvio.flightnode.rest.airline.Airline;
import org.alvio.flightnode.rest.aircraft.Aircraft;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AirlineMapper {

    public static AirlineDTO toDTO(Airline airline) {
        return toDTO(airline, false);
    }

    public static AirlineDTO toDTO(Airline airline, boolean showAircrafts) {
        AirlineDTO dto = new AirlineDTO(
                airline.getId(),
                airline.getName()
        );

        if (showAircrafts && airline.getAircrafts() != null) {
            List<AircraftSummaryDTO> aircrafts = airline.getAircrafts().stream()
                    .filter(Objects::nonNull)
                    .map(AircraftMapper::toSummary)
                    .sorted(Comparator.comparing(AircraftSummaryDTO::getId))
                    .toList();
            dto.setAircrafts(aircrafts);
        }

        return dto;
    }

    public static AirlineSummaryDTO toSummary(Airline airline) {
        return new AirlineSummaryDTO(
                airline.getId(),
                airline.getName()
        );
    }
}
