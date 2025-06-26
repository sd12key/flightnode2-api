package org.alvio.flightnode.dto;

public class AircraftSummaryDTO {
    private Long id;
    private String type;
    private String airlineName;

    public AircraftSummaryDTO(Long id, String type, String airlineName) {
        this.id = id;
        this.type = type;
        this.airlineName = airlineName;
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

}
