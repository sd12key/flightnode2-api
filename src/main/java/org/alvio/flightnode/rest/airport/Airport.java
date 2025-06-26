package org.alvio.flightnode.rest.airport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.city.City;
import org.alvio.flightnode.rest.flight.Flight;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Airport {
    @Id
    // @SequenceGenerator(name = "airport_sequence", sequenceName = "airport_sequence", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(generator = "airport_sequence")

    // set for MySQL only
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Airport name is required.")
    @Pattern(regexp = "^[a-zA-Z0-9 .'-/]+$", message = "Invalid airport name.")
    @Size(max = 50, message = "Maximum 50 characters.")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Airport code is required.")
    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "Code must be 3 letters.")
    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @NotNull(message = "City ID is required.")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "departureAirport", fetch = FetchType.EAGER)
    private List<Flight> departureFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport", fetch = FetchType.EAGER)
    private List<Flight> arrivalFlights = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public City getCity() {
        return city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setCode(String code) {
        this.code = code.trim().toUpperCase();
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Flight> getDepartureFlights() {
        return departureFlights;
    }

    public List<Flight> getArrivalFlights() {
        return arrivalFlights;
    }

}
