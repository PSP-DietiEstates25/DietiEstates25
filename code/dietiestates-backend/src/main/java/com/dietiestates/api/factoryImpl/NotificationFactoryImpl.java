package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.NotificationFactory;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.NotificationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFactoryImpl implements NotificationFactory {

	@Override
	public Notification createNotificationFromSpec(
			NotificationSpec spec,
			NotificationCategory notificationCategory,
			User user
			) {
		return Notification.builder()
				.message(spec.getMessage())
				.notificationCategory(notificationCategory)
				.user(user)
				.build();
	}

}
