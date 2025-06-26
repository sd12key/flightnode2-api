package org.alvio.flightnode.rest.passenger;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.flight.Flight;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Passenger {

    @Id
    // @SequenceGenerator(name = "passenger_sequence", sequenceName = "passenger_sequence", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(generator = "passenger_sequence")

    // set for MySQL only
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required.")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z .'-]*$",
            message = "English letters with optional spaces/hyphens/apostrophes only."
    )
    @Size(max = 30, message = "Maximum 30 characters.")
    @Column(nullable = false, length = 30)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9 .'’-]*$",
            message = "English letters, numbers, spaces, hyphens, apostrophes only."
    )
    @Size(max = 50, message = "Maximum 50 characters.")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits.")
    @Column(nullable = false, length = 10)
    private String phoneNumber;

    // Many-to-many : PASSENGERS <-> FLIGHTS
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "passenger_flight",
            joinColumns = @JoinColumn(name = "passenger_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"passenger_id", "flight_id"})
    )
    private List<Flight> flights = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName.trim(); }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = (flights != null) ? flights : new ArrayList<>();
    }

}
