package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.mapper.NotificationCategoryMapper;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.spec.NotificationCategorySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCategoryMapperDefaultImpl implements NotificationCategoryMapper {
	
	@Override
	public NotificationCategorySpec toSpec(NotificationCategoryRequest request) {
		return NotificationCategorySpec.builder()
				.name(request.getName())
				.isActive(request.getIsActive())
				.build();
	}
	
	@Override
	public NotificationCategoryResponse fromEntity(NotificationCategory notificationCategory) {
		return NotificationCategoryResponse.builder()
				.id(notificationCategory.getId())
				.createdDate(notificationCategory.getCreatedDate())
				.lastModifiedDate(notificationCategory.getLastModifiedDate())
				.name(notificationCategory.getName().toString())
				.isActive(notificationCategory.getIsActive())
				.build();
	}
}
