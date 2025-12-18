package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByUserException;
import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
	NotificationResponse createNotification(NotificationRequest request);
    void createNotificationsAfterRealEstateCreation(List<Search> searchesToNotify);
    NotificationResponse getNotificationById(Long notificationId, String userEmail) throws NotificationNotOwnedByUserException;
    Page<NotificationResponse> getUserNotifications(String userEmail, String notificationCategory, Integer page, Integer size);
    Page<NotificationResponse> getNegotiationNotifications(Long negotiationId, Integer page, Integer size);
}
