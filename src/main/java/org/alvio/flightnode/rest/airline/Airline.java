package org.alvio.flightnode.rest.airline;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.aircraft.Aircraft;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Airline name is required.")
    @Pattern(
        regexp = "^[A-Za-z0-9 &'’\\-.,()]+$",
        message = "Invalid airline name (only letters, numbers, spaces, hyphens, apostrophes, and basic punctuation allowed)."
    )
    @Size(max = 100, message = "Maximum 100 characters.")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "airline", fetch = FetchType.EAGER)
    private List<Aircraft> aircrafts = new ArrayList<>();

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Aircraft> getAircrafts() { return aircrafts; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name.trim(); }
}
