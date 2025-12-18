package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.spec.NotificationSpec;

public interface NotificationFactory {
    Notification createNotificationFromSpec(NotificationSpec spec, Negotiation negotiation);
}
