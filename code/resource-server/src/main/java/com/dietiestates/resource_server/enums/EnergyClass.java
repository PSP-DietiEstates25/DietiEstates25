package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum EnergyClass {
    A4(0),
    A3(1),
    A2(2),
    A1(3),
    B(4),
    C(5),
    D(6),
    E(7),
    F(8),
    G(9);

    @Getter
    private final Integer order;

    EnergyClass(Integer order){
        this.order = order;
    }

    public static Optional<EnergyClass> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (EnergyClass energyClass: values()) {
            if (energyClass.getOrder() == orderCode) return Optional.of(energyClass);
        }
        throw new IllegalArgumentException("Invalid energy class code: " + orderCode);
    }
}
