package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resource_server.finder.NotificationCategoryFinder;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.repository.NotificationCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFinderDefaultImpl implements NotificationCategoryFinder {

	private final NotificationCategoryRepository notificationCategoryRepository;
	
	@Override
	public NotificationCategory getNotificationCategoryByName(String name)
			throws NotificationCategoryNotFoundException {
		return notificationCategoryRepository.findByName(
				NotificationCategoryType.valueOf(name))
				.orElseThrow(NotificationCategoryNotFoundException::new);
	}

    @Override
    public NotificationCategory getNotificationCategoryByNameAndUserId(String name, Long userId)
            throws NotificationCategoryNotFoundException {

        return notificationCategoryRepository.findByNameAndUserId(
                name.toUpperCase(),
                userId
        ).orElseThrow(NotificationCategoryNotFoundException::new);
    }

    @Override
    public List<NotificationCategory> getUserNotificationCategories(Long userId) {
        return notificationCategoryRepository.findByUserId(userId);
    }

}
