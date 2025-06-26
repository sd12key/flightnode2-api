package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.*;
import org.alvio.flightnode.rest.city.City;
import org.alvio.flightnode.rest.airport.Airport;

import java.util.List;
import java.util.stream.Collectors;

public class CityMapper {
    public static CityDTO toCityDTO(City city, boolean includeAirports) {
        List<AirportSummaryDTO> airports = includeAirports && city.getAirports() != null
                ? city.getAirports().stream()
                .map(AirportMapper::toSummary)
                .collect(Collectors.toList())
                : null;

        return new CityDTO(city.getId(), city.getName(), city.getState(), city.getPopulation(), airports);
    }

    public static CitySummaryDTO toSummary(City city) {
        return new CitySummaryDTO(city.getId(), city.getName(), city.getState());
    }
}
