package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.spec.NotificationSpec;

public interface NotificationMapper {

	NotificationSpec toSpec(NotificationRequest request);
	
	NotificationResponse fromEntity(Notification notification);
}
