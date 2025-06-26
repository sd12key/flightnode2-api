package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.AircraftSummaryDTO;
import org.alvio.flightnode.dto.AirportSummaryDTO;
import org.alvio.flightnode.dto.FlightDTO;
import org.alvio.flightnode.dto.FlightSummaryDTO;
import org.alvio.flightnode.rest.flight.Flight;

public class FlightMapper {
    public static FlightDTO toFlightDTO(Flight flight) {
        AircraftSummaryDTO aircraftDto = new AircraftSummaryDTO(
                flight.getAircraft().getId(),
                flight.getAircraft().getType(),
                flight.getAircraft().getAirlineName()
        );

        AirportSummaryDTO departureDto = new AirportSummaryDTO(
                flight.getDepartureAirport().getId(),
                flight.getDepartureAirport().getName(),
                flight.getDepartureAirport().getCode()
        );

        AirportSummaryDTO arrivalDto = new AirportSummaryDTO(
                flight.getArrivalAirport().getId(),
                flight.getArrivalAirport().getName(),
                flight.getArrivalAirport().getCode()
        );

        return new FlightDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                aircraftDto,
                departureDto,
                arrivalDto
        );
    }

    public static FlightSummaryDTO toFlightSummaryDTO(Flight flight) {
        return new FlightSummaryDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getDepartureAirport().getCode(),
                flight.getArrivalAirport().getCode(),
                flight.getAircraft().getType(),
                flight.getAircraft().getAirlineName()
        );
    }
}
