package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationFinder {
	Notification getNotificationById(Long id) throws NotificationNotFoundException;
    Page<Notification> getNotificationCategoryNotifications(Long notificationCategoryId, Pageable pageable);
}
