package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum RealEstateCategory {
    SALE(0),
    RENT(1);

    @Getter
    private final int order;

    RealEstateCategory(int order){
        this.order = order;
    }

    public static Optional<RealEstateCategory> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (RealEstateCategory adCategory: values()) {
            if (adCategory.getOrder() == orderCode) return Optional.of(adCategory);
        }
        throw new IllegalArgumentException("Invalid ad category code: " + orderCode);
    }
}

