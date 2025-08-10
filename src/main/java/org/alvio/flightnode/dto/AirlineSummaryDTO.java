package org.alvio.flightnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirlineSummaryDTO {
    private Long id;
    private String name;

    public AirlineSummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
