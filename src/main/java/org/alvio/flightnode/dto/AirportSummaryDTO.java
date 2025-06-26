package org.alvio.flightnode.dto;

public class AirportSummaryDTO {
    private Long id;
    private String name;
    private String code;

    public AirportSummaryDTO(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
}
