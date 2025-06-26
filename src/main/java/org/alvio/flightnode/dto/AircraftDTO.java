package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AircraftDTO {
    private Long id;
    private String type;
    private String airlineName;
    private int capacity;
    private List<AirportSummaryDTO> departureAirports;
    private List<AirportSummaryDTO> arrivalAirports;

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

    public void setDepartureAirports(List<AirportSummaryDTO> departureAirports) {
        this.departureAirports = departureAirports;
    }

    public void setArrivalAirports(List<AirportSummaryDTO> arrivalAirports) {
        this.arrivalAirports = arrivalAirports;
    }

    public List<AirportSummaryDTO> getDepartureAirports() {
        return departureAirports;
    }

    public List<AirportSummaryDTO> getArrivalAirports() {
        return arrivalAirports;
    }
}
