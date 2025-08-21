package com.dietiestates.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateDetailRequest(
        @NotNull @Valid ServicesDTO services,
        @NotNull @Valid GeographicalPositionDTO geo) {

    public record ServicesDTO(
            @NotNull Boolean hasAirConditioning,
            @NotNull Boolean hasDoorman,
            @NotNull Boolean hasElevator) {
    }

    public record GeographicalPositionDTO(
            @NotBlank String city,
            @NotBlank String municipality,
            @NotNull @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") Double zoneMarkerLatitude,
            @NotNull @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") Double zoneMarkerLongitude,
            @NotNull @Positive Float zoneMarkerRadius) {
    }
}
