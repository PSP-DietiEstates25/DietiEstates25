package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum NotificationCategoryType {
    NEW_PROPERTIES(0),
    PROMOTIONAL(1),
    VISIT(2),
    OFFER(3);

    @Getter
    private final int order;

    NotificationCategoryType(int order){
        this.order = order;
    }

    public static Optional<NotificationCategoryType> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (NotificationCategoryType notificationCategoryType: values()) {
            if (notificationCategoryType.getOrder() == orderCode) return Optional.of(notificationCategoryType);
        }
        throw new IllegalArgumentException("Invalid notification category type code: " + orderCode);
    }
}
