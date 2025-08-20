package com.dietiestates.api.dto;

import jakarta.validation.constraints.*;

public record CreateDetailRequest(
        @NotNull ServicesDTO services,
        @NotNull GeographicalPositionDTO geo) {
    public record ServicesDTO(
            @NotNull Boolean hasAirConditioning,
            @NotNull Boolean hasDoorman,
            @NotNull Boolean hasElevator) {
    }

    public record GeographicalPositionDTO(
            @NotBlank String city,
            @NotBlank String municipality,
            @NotNull Double zoneMarkerLatitude,
            @NotNull Double zoneMarkerLongitude,
            @NotNull Float zoneMarkerRadius) {
    }
}