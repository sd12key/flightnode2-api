package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GateDTO {

    private Long id;
    private String name;
    private Long airportId;

    public GateDTO(Long id, String name, Long airportId) {
        this.id = id;
        this.name = name;
        this.airportId = airportId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getAirportId() { return airportId; }

}
