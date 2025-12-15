package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum EnergyClass {
    G(0),
    F(1),
    E(2),
    D(3),
    C(4),
    B(5),
    A1(6),
    A2(7),
    A3(8),
    A4(9);

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
