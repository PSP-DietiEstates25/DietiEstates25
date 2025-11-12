package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface NotificationService {
	NotificationResponse createNotification(String notificationCategoryName, NotificationRequest request);
    void createNotificationsAfterRealEstateCreation(List<Search> searchesToNotify);
    NotificationResponse getNotificationById(String notificationCategoryName, Long notificationId) throws NotificationNotOwnedByNotificationCategoryException;
    Page<NotificationResponse> getNotificationCategoryNotifications(String notificationCategoryName, Integer page, Integer size);
}
