package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.dto.response.NotificationCategoryResponse;
import com.dietiestates.api.factory.NotificationCategoryFactory;
import com.dietiestates.api.finder.NotificationCategoryFinder;
import com.dietiestates.api.mapper.NotificationCategoryMapper;
import com.dietiestates.api.repository.NotificationCategoryRepository;
import com.dietiestates.api.service.NotificationCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCategoryServiceImpl implements NotificationCategoryService {

	private final NotificationCategoryRepository notificationCategoryRepository;
	private final NotificationCategoryFactory notificationCategoryFactory;
	private final NotificationCategoryFinder notificationCategoryFinder;
	private final NotificationCategoryMapper notificationCategoryMapper;
	
	@Override
	public void createNotificationCategory(NotificationCategoryRequest request) {
		
		var notificationCategorySpec = notificationCategoryMapper.toSpec(request);
		
		var notificationCategory = notificationCategoryFactory.createNotificationCategoryFromSpec(notificationCategorySpec);
		notificationCategoryRepository.save(notificationCategory);
	}
	
	@Override
	public NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName) {
		
		var notificationCategory = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName);
		return notificationCategoryMapper.fromEntity(notificationCategory);
	}
}
