package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.*;
import org.alvio.flightnode.rest.airport.Airport;
import org.alvio.flightnode.rest.city.City;

public class AirportMapper {
    public static AirportDTO toAirportDTO(Airport airport) {
        City c = airport.getCity();
        CitySummaryDTO city = new CitySummaryDTO(c.getId(), c.getName(), c.getState());

        return new AirportDTO(airport.getId(), airport.getName(), airport.getCode(), city);
    }

    public static AirportSummaryDTO toSummaryNoCity(Airport airport) {
        return new AirportSummaryDTO(airport.getId(), airport.getName(), airport.getCode());
    }
}
