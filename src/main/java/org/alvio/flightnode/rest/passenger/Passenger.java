package org.alvio.flightnode.rest.passenger;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Passenger {

    @Id
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

    @NotBlank(message = "Last name is required")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = (firstName != null) ? firstName.trim() : null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = (lastName != null) ? lastName.trim() : null;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = (phoneNumber != null) ? phoneNumber.trim() : null;
    }

}
