package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByUserException;

public interface NotificationVerifier {
    void checkNotificationExists(Long id) throws NotificationNotFoundException;
}
