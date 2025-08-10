package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirportDTO {
    private Long id;
    private String name;
    private String code;
    private CitySummaryDTO city;
    private List<GateSummaryDTO> gates;

    public AirportDTO(Long id, String name, String code, CitySummaryDTO city) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public AirportDTO(Long id, String name, String code, CitySummaryDTO city, List<GateSummaryDTO> gates) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
        this.gates = gates;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public CitySummaryDTO getCity() { return city; }
    public List<GateSummaryDTO> getGates() { return gates; }
}
