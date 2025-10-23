package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.NotificationRequest;
import com.dietiestates.resourceserver.dto.response.NotificationResponse;
import com.dietiestates.resourceserver.model.Notification;
import com.dietiestates.resourceserver.spec.NotificationSpec;

public interface NotificationMapper {

	NotificationSpec toSpec(NotificationRequest request);
	
	NotificationResponse fromEntity(Notification notification);
}
