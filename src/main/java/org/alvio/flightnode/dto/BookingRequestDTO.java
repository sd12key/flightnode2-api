package org.alvio.flightnode.dto;

import jakarta.validation.constraints.NotNull;

public class BookingRequestDTO {

    @NotNull(message = "passengerId is required.")
    private Long passengerId;

    @NotNull(message = "flightId is required.")
    private Long flightId;

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
}
