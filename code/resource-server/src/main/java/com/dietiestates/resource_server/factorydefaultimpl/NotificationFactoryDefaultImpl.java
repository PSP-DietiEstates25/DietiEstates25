package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.model.Negotiation;
import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.NotificationFactory;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.spec.NotificationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFactoryDefaultImpl implements NotificationFactory {

    @Override
    public Notification createNotificationFromSpec(
            NotificationSpec spec,
            Negotiation negotiation
    ) {
        return Notification.builder()
                .message(spec.getMessage())
                .notificationCategory(spec.getNotificationCategory())
                .isVisible(spec.getIsVisible())
                .negotiation(negotiation)
                .build();
    }

}
