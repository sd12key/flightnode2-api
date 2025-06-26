package org.alvio.flightnode.dto;

public class CitySummaryDTO {
    private Long id;
    private String name;
    private String state;
//    private int population;

    public CitySummaryDTO(Long id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
//        this.population = population;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getState() { return state; }
//    public int getPopulation() { return population; }
}
