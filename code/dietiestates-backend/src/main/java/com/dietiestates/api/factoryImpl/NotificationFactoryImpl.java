package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.factory.NotificationFactory;
import com.dietiestates.api.model.Notification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFactoryImpl implements NotificationFactory {

	@Override
	public Notification createNotification(NotificationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
