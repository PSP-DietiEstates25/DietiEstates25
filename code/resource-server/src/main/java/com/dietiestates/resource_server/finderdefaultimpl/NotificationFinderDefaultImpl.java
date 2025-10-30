package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.finder.NotificationFinder;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFinderDefaultImpl implements NotificationFinder {

	private final NotificationRepository notificationRepository;

	@Override
	public Notification getNotificationById(Long id)
			throws NotificationNotFoundException {
		return notificationRepository.findById(id)
				.orElseThrow(NotificationNotFoundException::new);
	}

    @Override
    public List<Notification> getPrincipalNotifications(User user, NotificationCategory notificationCategory) {
        return notificationRepository.findByUserAndNotificationCategory(user, notificationCategory);
    }

}
