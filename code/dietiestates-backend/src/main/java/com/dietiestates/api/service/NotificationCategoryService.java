package com.dietiestates.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.repository.NotificationCategoryRepository;

@Service
public class NotificationCategoryService {

	@Autowired
	private NotificationCategoryRepository notificationCategoryRepository;
	
	public NotificationCategory createNotificationCategory(String name) {
		
		NotificationCategory notificationCategory = new NotificationCategory();
		NotificationCategoryType notificationCategoryName = checkValidCategoryName(name);
		
		if (notificationCategoryName != null) {
			notificationCategory.setName(notificationCategoryName);
		}
		
		saveNotificationCategory(notificationCategory);
		
		return notificationCategory;
	}
	
	public Optional<NotificationCategory> getNotificationCategoryByName(String name) {
		
		Optional<NotificationCategory> notificationCategory = null;
		NotificationCategoryType notificationCategoryType = checkValidCategoryName(name);
		
		if(notificationCategoryType != null)
			notificationCategory = notificationCategoryRepository.findById(null);
			
		return notificationCategory;
	}
	
	public NotificationCategoryType checkValidCategoryName(String name) throws IllegalArgumentException {
		
		NotificationCategoryType notificationCategoryName = null;
		
		try {
			notificationCategoryName = NotificationCategoryType.valueOf(name);
		} catch (IllegalArgumentException e) {
			System.out.println("Il nome della categoria inserito non esiste");
		}
		
		return notificationCategoryName;
	}
	
	public void saveNotificationCategory(NotificationCategory notificationCategory) {
		
		try {
			notificationCategoryRepository.save(notificationCategory);
		} catch (Exception e) {
			System.out.println("salvataggio categoria non effettuato");
		}
		
	}
}
