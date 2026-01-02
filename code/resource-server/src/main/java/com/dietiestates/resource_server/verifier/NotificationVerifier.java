package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;

public interface NotificationVerifier {
    void checkNotificationExists(Long id) throws NotificationNotFoundException;
}
