package org.alvio.flightnode.dto;

public class AircraftSummaryDTO {
    private Long id;
    private String type;
    private String airlineName;
    private int capacity;

    public AircraftSummaryDTO(Long id, String type, String airlineName, int capacity) {
        this.id = id;
        this.type = type;
        this.airlineName = airlineName;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() { return capacity; }

    public String getAirlineName() {
        return airlineName;
    }

}
