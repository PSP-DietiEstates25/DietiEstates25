package com.dietiestates.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietiestates.api.model.Notification;
import com.dietiestates.api.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private NotificationCategoryService notificationCategoryService;
	
	public Notification createNotification(String message, String notificationCategoryName) {
		
		Notification notification = new Notification();
		//NotificationCategory notificationCategory = notificationCategoryService.getNotificationCategoryByName(notificationCategoryName);
		
		saveNotification(notification);
		
		return notification;
	}
		
	public void saveNotification(Notification notification) {
		try {
			notificationRepository.save(notification);
		} catch(Exception e) {
			System.out.println("Failed to save new notification");
		}
	}
}
