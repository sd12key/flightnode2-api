package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.*;
import org.alvio.flightnode.rest.airport.Airport;
import org.alvio.flightnode.rest.city.City;

import java.util.List;

public class AirportMapper {
    public static AirportDTO toAirportDTO(Airport airport) {
        City c = airport.getCity();
        CitySummaryDTO city = new CitySummaryDTO(c.getId(), c.getName(), c.getState());

        return new AirportDTO(airport.getId(), airport.getName(), airport.getCode(), city);
    }

    public static AirportDTO toAirportDTO(Airport airport, boolean showGates) {
        City c = airport.getCity();
        CitySummaryDTO city = new CitySummaryDTO(c.getId(), c.getName(), c.getState());

        List<GateSummaryDTO> gates = showGates
                ? GateMapper.toSummaryList(airport.getGates())
                : null;

        return new AirportDTO(
                airport.getId(),
                airport.getName(),
                airport.getCode(),
                city,
                gates
        );
    }


    public static AirportSummaryDTO toSummary(Airport airport) {
        return new AirportSummaryDTO(airport.getId(), airport.getName(),
                airport.getCode(), airport.getCity().getName(), airport.getCity().getState());
    }
}


