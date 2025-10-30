package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.mapper.NotificationMapper;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.spec.NotificationSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMapperDefaultImpl implements NotificationMapper {
	
	@Override
	public NotificationSpec toSpec(NotificationRequest request) {
		return NotificationSpec.builder()
				.message(request.getMessage())
				//.notificationCategoryName(request.getNotificationCategoryName())
				.userEmail(request.getUserEmail())
				.build();
	}
	
	@Override
	public NotificationResponse fromEntity(Notification notification) {
		return NotificationResponse.builder()
				.id(notification.getId())
				.createdDate(notification.getCreatedDate())
				.lastModifiedDate(notification.getLastModifiedDate())
				.message(notification.getMessage())
				.notificationCategoryId(notification.getNotificationCategory().getId())
				.userEmail(notification.getUser().getEmail())
				.build();
	}

    @Override
    public List<NotificationResponse> createNotificationsResponse(List<Notification> notifications){
        var notificationsResponse = new ArrayList<NotificationResponse>();
        notifications.forEach((notification) -> {
            notificationsResponse.add(this.fromEntity(notification));
        });

        return notificationsResponse;
    }
}
