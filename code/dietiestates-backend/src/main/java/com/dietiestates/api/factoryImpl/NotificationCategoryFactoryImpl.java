package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.factory.NotificationCategoryFactory;
import com.dietiestates.api.model.NotificationCategory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFactoryImpl implements NotificationCategoryFactory {

	@Override
	public NotificationCategory createNotificationCategory(NotificationCategoryRequest request) {
		return null;
	}

}
