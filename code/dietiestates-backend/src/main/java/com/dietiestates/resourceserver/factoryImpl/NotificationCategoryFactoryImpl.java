package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.NotificationCategoryFactory;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.spec.NotificationCategorySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFactoryImpl implements NotificationCategoryFactory {

	@Override
	public NotificationCategory createNotificationCategoryFromSpec(
			NotificationCategorySpec spec
			) {
		return NotificationCategory.builder()
				.name(spec.getName())
				.isActive(spec.getIsActive())
				.build();
	}

}
