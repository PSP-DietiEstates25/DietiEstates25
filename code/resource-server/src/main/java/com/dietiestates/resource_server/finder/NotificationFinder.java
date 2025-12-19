package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationFinder {
	Notification getNotificationById(Long id) throws NotificationNotFoundException;
    Page<Notification> getUserNotifications(Long userId, List<String> notificationCategories, Pageable pageable);
    Page<Notification> getNegotiationNotifications(Long negotiationId, Pageable pageable);
    List<Notification> extractAllNegotiationsNotifications(List<Negotiation> negotiations, List<String> notificationCategories);
}
