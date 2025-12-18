package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByUserException;
import com.dietiestates.resource_server.repository.NotificationRepository;
import com.dietiestates.resource_server.verifier.NotificationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationVerifierDefaultImpl implements NotificationVerifier {

    private final NotificationRepository notificationRepository;

    @Override
    public void checkNotificationExists(Long id) throws NotificationNotFoundException {
        if(!notificationRepository.existsById(id))
            throw new NotificationNotFoundException();
    }

    /*
    @Override
    public void checkNotificationOwnedByUser(
            Long id,
            String userEmail
    ) throws NotificationNotOwnedByUserException {

        if(!notificationRepository.exists(id, NotificationCategory.valueOf(notificationCategoryName)))
            throw new NotificationNotOwnedByUserException();
    }

     */
}
