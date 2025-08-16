package com.dietiestates.api.dto;

import java.math.BigDecimal;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;

import jakarta.validation.constraints.*;

public record CreateRealEstateAdRequest(
        @NotNull AdCategory category, // SALE | RENT
        @NotBlank String description,

        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotNull @Positive Float size,
        @NotBlank String address,
        @NotNull @Positive Integer rooms,
        @NotNull Integer floor,
        @NotNull EnergyClass energyClass,
        @NotNull Double latitude,
        @NotNull Double longitude,

        @NotNull Long detailId 
) {
}
