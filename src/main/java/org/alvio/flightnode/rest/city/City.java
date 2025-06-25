package org.alvio.flightnode.rest.city;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(
        name = "city",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "state"})
)
public class City {

    @Id
    // @SequenceGenerator(name = "city_sequence", sequenceName = "city_sequence", allocationSize = 20, initialValue = 1)
    // @GeneratedValue(generator = "city_sequence")

    // MySQL -> Use IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "City name is required")
    @Pattern(regexp = "^[a-zA-Z .'-]+$", message = "Invalid city name")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "State must be 2-letters")
    @Column(nullable = false, length = 2)
    private String state;

    @Min(value = 1, message = "Population must be positive")
    private int population;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = (state != null) ? state.trim().toUpperCase() : null;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
