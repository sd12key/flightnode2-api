package org.alvio.flightnode.dto;

public class AirportSummaryDTO {
    private Long id;
    private String name;
    private String code;
    private String cityName;
    private String cityState;

    public AirportSummaryDTO(Long id, String name, String code, String cityName, String cityState) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cityName = cityName;
        this.cityState = cityState;
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

    public String getCityState() { return cityState; }
}
