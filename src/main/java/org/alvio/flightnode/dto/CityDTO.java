package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDTO {
    private Long id;
    private String name;
    private String state;
    private int population;
    private List<AirportSummaryDTO> airports; // optional: may be null if not included

    public CityDTO(Long id, String name, String state, int population, List<AirportSummaryDTO> airports) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.population = population;
        this.airports = airports;
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public String getState() { return state; }
    public int getPopulation() { return population; }
    public List<AirportSummaryDTO> getAirports() { return airports; }
}
