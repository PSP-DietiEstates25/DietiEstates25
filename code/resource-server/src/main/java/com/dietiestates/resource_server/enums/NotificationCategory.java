package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum NotificationCategory {
    NEW_PROPERTIES(0),
    PROMOTIONAL(1),
    VISIT(2),
    OFFER(3);

    @Getter
    private final int order;

    NotificationCategory(int order){
        this.order = order;
    }

    public static Optional<NotificationCategory> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (NotificationCategory notificationCategory : values()) {
            if (notificationCategory.getOrder() == orderCode) return Optional.of(notificationCategory);
        }
        throw new IllegalArgumentException("Invalid notification category type code: " + orderCode);
    }
}
