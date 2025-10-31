package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resource_server.finder.NotificationCategoryFinder;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.repository.NotificationCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

}
