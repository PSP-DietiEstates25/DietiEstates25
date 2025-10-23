package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.NotificationCategoryRequest;
import com.dietiestates.resourceserver.dto.response.NotificationCategoryResponse;
import com.dietiestates.resourceserver.factory.NotificationCategoryFactory;
import com.dietiestates.resourceserver.finder.NotificationCategoryFinder;
import com.dietiestates.resourceserver.mapper.NotificationCategoryMapper;
import com.dietiestates.resourceserver.repository.NotificationCategoryRepository;
import com.dietiestates.resourceserver.service.NotificationCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCategoryServiceImpl implements NotificationCategoryService {

	private final NotificationCategoryRepository notificationCategoryRepository;
	private final NotificationCategoryFactory notificationCategoryFactory;
	private final NotificationCategoryFinder notificationCategoryFinder;
	private final NotificationCategoryMapper notificationCategoryMapper;
	
	@Override
	public NotificationCategoryResponse createNotificationCategory(NotificationCategoryRequest request) {
		
		var notificationCategorySpec = notificationCategoryMapper.toSpec(request);
		
		var notificationCategory = notificationCategoryFactory.createNotificationCategoryFromSpec(notificationCategorySpec);
		notificationCategoryRepository.save(notificationCategory);
		
		return notificationCategoryMapper.fromEntity(notificationCategory);
	}
	
	@Override
	public NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName) {
		
		var notificationCategory = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName);
		return notificationCategoryMapper.fromEntity(notificationCategory);
	}
}
