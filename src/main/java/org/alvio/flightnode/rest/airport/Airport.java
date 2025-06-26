package org.alvio.flightnode.rest.airport;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.flightnode.rest.city.City;

@Entity
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Airport name is required")
    @Pattern(regexp = "^[a-zA-Z0-9 .'-]+$", message = "Invalid airport name")
    @Size(max = 50, message = "Maximum 50 characters")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Airport code is required")
    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "Code must be 3 letters")
    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;


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
        this.name = (name != null) ? name.trim() : null;
    }

    public void setCode(String code) {
        this.code = (code != null) ? code.trim().toUpperCase() : null;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
