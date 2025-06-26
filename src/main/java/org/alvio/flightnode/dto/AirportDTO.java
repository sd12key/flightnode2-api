package org.alvio.flightnode.dto;

public class AirportDTO {
    private Long id;
    private String name;
    private String code;
    private CitySummaryDTO city;  // small embedded city, not full object

    public AirportDTO(Long id, String name, String code, CitySummaryDTO city) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public CitySummaryDTO getCity() { return city; }
}
