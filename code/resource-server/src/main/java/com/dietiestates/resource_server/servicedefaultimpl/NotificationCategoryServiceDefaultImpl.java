package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.factory.NotificationCategoryFactory;
import com.dietiestates.resource_server.finder.NotificationCategoryFinder;
import com.dietiestates.resource_server.mapper.NotificationCategoryMapper;
import com.dietiestates.resource_server.repository.NotificationCategoryRepository;
import com.dietiestates.resource_server.service.NotificationCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCategoryServiceDefaultImpl implements NotificationCategoryService {

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

    @Override
    public void updateNotificationCategory(String notificationCategoryName, NotificationCategoryRequest request) {

        var notificationCategoryToUpdate = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName);
        notificationCategoryToUpdate.setName(NotificationCategoryType.valueOf(request.getName()));
        notificationCategoryToUpdate.setIsActive(request.getIsActive());

        notificationCategoryRepository.save(notificationCategoryToUpdate);
    }
}
