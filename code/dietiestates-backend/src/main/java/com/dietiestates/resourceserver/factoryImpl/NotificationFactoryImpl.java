package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.NotificationFactory;
import com.dietiestates.resourceserver.model.Notification;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.NotificationSpec;

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
