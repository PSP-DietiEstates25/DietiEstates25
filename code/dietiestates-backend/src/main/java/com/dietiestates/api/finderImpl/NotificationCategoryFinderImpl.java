package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.api.finder.NotificationCategoryFinder;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.repository.NotificationCategoryRepository;

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
