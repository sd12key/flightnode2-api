package org.alvio.flightnode.rest.aircraft;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Aircraft {

    @Id
    // @SequenceGenerator(name = "aircraft_sequence", sequenceName = "aircraft_sequence", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(generator = "aircraft_sequence")

    // set for MySQL only
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Aircraft model is required.")
    @Pattern(regexp = "^[A-Za-z0-9 .'-]+$", message = "Invalid aircraft model.")
    @Size(max = 100, message = "Maximum 100 characters.")
    @Column(nullable = false, length = 100)
    private String model;

    @NotBlank(message = "Airline name is required.")
    @Pattern(
            regexp = "^[A-Za-z0-9 &'’\\-.,()]+$",
            message = "Invalid airline name (only letters, numbers, spaces, hyphens, apostrophes, and basic punctuation allowed)."
    )
    @Size(max = 100, message = "Maximum 100 characters.")
    @Column(nullable = false, length = 100)
    private String airlineName;

    @Min(value = 1, message = "Capacity must be a positive number.")
    @Column(nullable = false)
    private int capacity;

    // later: relationship to flights (one aircraft -> many flights)
    // @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Flight> flights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = (model != null) ? model.trim() : null;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = (airlineName != null) ? airlineName.trim() : null;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
