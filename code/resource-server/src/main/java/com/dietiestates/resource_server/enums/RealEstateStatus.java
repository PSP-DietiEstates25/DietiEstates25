package com.dietiestates.resource_server.enums;

import lombok.Getter;

import java.util.Optional;

public enum RealEstateStatus {
    ACTIVE(0),
    DELETED(1);

    @Getter
    private final int order;

    RealEstateStatus(int order){
        this.order = order;
    }

    public static Optional<RealEstateStatus> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (RealEstateStatus realEstateStatus: values()) {
            if (realEstateStatus.getOrder() == orderCode) return Optional.of(realEstateStatus);
        }
        throw new IllegalArgumentException("Invalid real estate status code: " + orderCode);
    }
}
