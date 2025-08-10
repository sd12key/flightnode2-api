package org.alvio.flightnode.rest.gate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.airport.Airport;

@Entity
@Table(name = "gate", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"gate_name", "airport_id"})
        })

public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Gate name is required.")
    @Pattern(regexp = "^[a-zA-Z0-9 .'-/]+$", message = "Invalid gate name.")
    @Size(max = 20, message = "Maximum 20 characters.")
    @Column(nullable = false, length = 20)
    private String name;

    @NotNull(message = "Airport ID is required.")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_id", nullable = false)
    private Airport airport;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.trim().toUpperCase();
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }
}



