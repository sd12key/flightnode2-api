package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.*;
import org.alvio.flightnode.rest.flight.Flight;

public class FlightMapper {
    public static FlightDTO toFlightDTO(Flight flight) {
        AircraftSummaryDTO aircraftDto = new AircraftSummaryDTO(
                flight.getAircraft().getId(),
                flight.getAircraft().getType(),
                flight.getAircraft().getAirline().getName(),
                flight.getAircraft().getCapacity()
        );

        AirportSummaryDTO departureDto = new AirportSummaryDTO(
                flight.getDepartureAirport().getId(),
                flight.getDepartureAirport().getName(),
                flight.getDepartureAirport().getCode(),
                flight.getDepartureAirport().getCity().getName(),
                flight.getDepartureAirport().getCity().getState()

        );

        GateSummaryDTO departureGateDto = new GateSummaryDTO (flight.getDepartureGate().getId(),
                                                              flight.getDepartureGate().getName()
        );

        AirportSummaryDTO arrivalDto = new AirportSummaryDTO(
                flight.getArrivalAirport().getId(),
                flight.getArrivalAirport().getName(),
                flight.getArrivalAirport().getCode(),
                flight.getArrivalAirport().getCity().getName(),
                flight.getArrivalAirport().getCity().getState()
        );

        GateSummaryDTO arrivalGateDto = new GateSummaryDTO (flight.getArrivalGate().getId(),
                flight.getArrivalGate().getName()
        );

        return new FlightDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                aircraftDto,
                departureDto,
                departureGateDto,
                arrivalDto,
                arrivalGateDto
        );
    }

    public static FlightDTO toFlightDTO(Flight flight, boolean showPassengers) {
        FlightDTO dto = toFlightDTO(flight);

        if (showPassengers && flight.getPassengers() != null) {
            dto.setPassengers(
                    flight.getPassengers().stream()
                            .map(PassengerMapper::toSummary)
                            .toList()
            );
        }

        return dto;
    }

    public static FlightSummaryDTO toSummary(Flight flight) {
        return new FlightSummaryDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getDepartureAirport().getCode(),
                flight.getArrivalAirport().getCode(),
                flight.getAircraft().getType(),
                flight.getAircraft().getAirline().getName()
        );
    }
}
