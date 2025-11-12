package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.spec.NotificationSpec;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationMapper {
	NotificationSpec toSpec(NotificationRequest request);
	NotificationResponse fromEntity(Notification notification);
    List<NotificationResponse> createNotificationsResponse(List<Notification> notifications);

    Page<NotificationResponse> createPagedNotificationsResponse(Page<Notification> notifications);
}
