package com.dietiestates.api.service;

import org.springframework.data.domain.Page;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationService {

	NotificationResponse createNotification(
			String notificationCategoryName,
			NotificationRequest request);

	NotificationResponse getNotificationById(
			String notificationCategoryName,
			Long notificationId)
			throws NotificationNotOwnedByNotificationCategoryException;

	Page<NotificationResponse> listMyNotifications(String userEmail, int page, int size);

	Page<NotificationResponse> listMyNotifications(
			String userEmail,
			String notificationCategoryName,
			int page,
			int size);

}
