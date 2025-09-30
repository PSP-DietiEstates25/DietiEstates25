package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.mapper.NotificationCategoryMapper;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.repository.NotificationCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCategoryService {

	private final NotificationCategoryRepository notificationCategoryRepository;
	private final NotificationCategoryMapper notificationCategoryMapper;
	
	public void createNotificationCategory(NotificationCategoryRequest request) {
		var notificationCategory = notificationCategoryMapper.toEntity(request);
		notificationCategoryRepository.save(notificationCategory);
	}
	
	public NotificationCategory of(NotificationCategoryRequest request) {
		return NotificationCategory.builder()
				.createdDate(LocalDateTime.now())
				.name(NotificationCategoryType.valueOf(request.getName()))
				.isActive(request.getIsActive())
				.build();
	}
}
