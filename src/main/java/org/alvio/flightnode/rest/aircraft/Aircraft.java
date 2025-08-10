package org.alvio.flightnode.rest.aircraft;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.airline.Airline;
import org.alvio.flightnode.rest.flight.Flight;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Aircraft {

    @Id
    // @SequenceGenerator(name = "aircraft_sequence", sequenceName = "aircraft_sequence", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(generator = "aircraft_sequence")

    // set for MySQL only
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Aircraft type is required.")
    @Pattern(regexp = "^[A-Za-z0-9 .'-]+$", message = "Invalid aircraft type.")
    @Size(max = 100, message = "Maximum 100 characters.")
    @Column(nullable = false, length = 100)
    private String type;

    @NotNull(message = "Airline is required.")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Min(value = 1, message = "Capacity must be a positive number.")
    @Column(nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.EAGER)
    private List<Flight> flights = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.trim();
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}
