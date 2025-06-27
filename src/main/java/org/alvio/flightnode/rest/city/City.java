package org.alvio.flightnode.rest.city;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.alvio.flightnode.rest.airport.Airport;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "city",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "state"})
)
public class City {

    @Id
    // @SequenceGenerator(name = "city_sequence", sequenceName = "city_sequence", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(generator = "city_sequence")

    // set for MySQL only
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required.")
    @Pattern(regexp = "^[a-zA-Z .'-]+$", message = "Invalid city name.")
    @Size(max = 50, message = "Maximum 50 characters.")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "State is required.")
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "State must be 2-letters.")
    @Column(nullable = false, length = 2)
    private String state;

    @Min(value = 1, message = "Population must be positive.")
    private int population;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Airport> airports = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state.trim().toUpperCase();
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<Airport> getAirports() {
        return airports;
    }
}
