package org.alvio.flightnode.dto;

public class GateSummaryDTO {

    private Long id;
    private String name;

    public GateSummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
