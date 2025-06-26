package org.alvio.flightnode.dto;

import java.util.List;

public class AircraftDTO {
    private Long id;
    private String type;
    private String airlineName;
    private int capacity;
//    private List<AirportSummaryDTO> departureAirports;
//    private List<AirportSummaryDTO> arrivalAirports;

    public AircraftDTO(Long id, String model, String airlineName, int capacity) {
        this.id = id;
        this.type = model;
        this.airlineName = airlineName;
        this.capacity = capacity;

    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public int getCapacity() {
        return capacity;
    }
}
