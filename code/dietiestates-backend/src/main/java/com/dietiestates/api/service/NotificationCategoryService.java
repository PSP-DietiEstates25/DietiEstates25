package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.NotificationCategoryDto;
import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.repository.NotificationCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCategoryService {

	private final NotificationCategoryRepository notificationCategoryRepository;
	
	public void createNotificationCategory(NotificationCategoryDto request) {
		var notificationCategory = of(request);
		notificationCategoryRepository.save(notificationCategory);
	}
	
	private NotificationCategory of(NotificationCategoryDto request) {
		return NotificationCategory.builder()
				.createdDate(LocalDateTime.now())
				.name(NotificationCategoryType.valueOf(request.getName()))
				.isActive(request.getIsActive())
				.build();
	}
}
