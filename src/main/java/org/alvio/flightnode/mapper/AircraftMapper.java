package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.AircraftDTO;
import org.alvio.flightnode.dto.AircraftSummaryDTO;
import org.alvio.flightnode.rest.aircraft.Aircraft;

public class AircraftMapper {
    public static AircraftDTO toAircraftDTO(Aircraft aircraft, boolean includeAirports) {
        // for now: returning base AircraftDTO
        return new AircraftDTO(
                aircraft.getId(),
                aircraft.getType(),
                aircraft.getAirlineName(),
                aircraft.getCapacity()
        );
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
