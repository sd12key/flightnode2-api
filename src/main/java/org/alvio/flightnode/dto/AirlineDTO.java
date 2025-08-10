package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirlineDTO {
    private Long id;
    private String name;
    private List<AircraftSummaryDTO> aircrafts;

    public AirlineDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public List<AircraftSummaryDTO> getAircrafts() { return aircrafts; }

    public void setAircrafts(List<AircraftSummaryDTO> aircrafts) {
        this.aircrafts = aircrafts;
    }
}
