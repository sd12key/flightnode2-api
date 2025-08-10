package org.alvio.flightnode.dto;

public class AirportSummaryDTO {
    private Long id;
    private String name;
    private String code;
    private String cityName;

    public AirportSummaryDTO(Long id, String name, String code, String cityName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCityName() { return cityName; }
}
