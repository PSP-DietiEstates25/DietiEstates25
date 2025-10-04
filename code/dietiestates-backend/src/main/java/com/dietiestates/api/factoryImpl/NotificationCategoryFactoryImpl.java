package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.factory.NotificationCategoryFactory;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.spec.NotificationCategorySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFactoryImpl implements NotificationCategoryFactory {

	@Override
	public NotificationCategory createNotificationCategoryFromSpec(
			NotificationCategorySpec spec
			) {
		return NotificationCategory.builder()
				.name(NotificationCategoryType.valueOf(spec.getName()))
				.isActive(spec.getIsActive())
				.build();
	}

}
