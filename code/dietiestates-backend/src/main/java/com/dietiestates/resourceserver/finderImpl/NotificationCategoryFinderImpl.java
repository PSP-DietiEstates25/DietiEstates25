package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.enums.NotificationCategoryType;
import com.dietiestates.resourceserver.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resourceserver.finder.NotificationCategoryFinder;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.repository.NotificationCategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFinderImpl implements NotificationCategoryFinder {

	private final NotificationCategoryRepository notificationCategoryRepository;
	
	@Override
	public NotificationCategory getNotificationCategoryByName(String name)
			throws NotificationCategoryNotFoundException {
		return notificationCategoryRepository.findByName(
				NotificationCategoryType.valueOf(name))
				.orElseThrow(NotificationCategoryNotFoundException::new);
	}

}
