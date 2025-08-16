package com.dietiestates.api.dto;

import java.math.BigDecimal;

public record RealEstateAdResponse(
        Long id,
        String category,
        String description,
        BigDecimal price,
        Float size,
        String address,
        Integer rooms,
        Integer floor,
        String energyClass,
        Double latitude,
        Double longitude,
        String estateAgentEmail,
        Long detailId) {
}
