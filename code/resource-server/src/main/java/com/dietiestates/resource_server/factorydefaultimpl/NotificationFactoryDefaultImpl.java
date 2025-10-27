package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.NotificationFactory;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NotificationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFactoryDefaultImpl implements NotificationFactory {

    @Override
    public Notification createNotificationFromSpec(
            NotificationSpec spec,
            NotificationCategory notificationCategory,
            User user
    ) {
        return Notification.builder()
                .message(spec.getMessage())
                .notificationCategory(notificationCategory)
                .user(user)
                .build();
    }

}
