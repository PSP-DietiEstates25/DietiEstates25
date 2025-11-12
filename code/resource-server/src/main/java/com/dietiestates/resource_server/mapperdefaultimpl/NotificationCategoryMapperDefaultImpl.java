package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.mapper.NotificationCategoryMapper;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.spec.NotificationCategorySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationCategoryMapperDefaultImpl implements NotificationCategoryMapper {
	
	@Override
	public NotificationCategorySpec toSpec(NotificationCategoryRequest request) {
		return NotificationCategorySpec.builder()
				.name(request.getName())
				.isActive(request.getIsActive())
                .userEmail(request.getUserEmail())
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
                .userEmail(notificationCategory.getUser().getEmail())
				.build();
	}

    @Override
    public List<NotificationCategoryResponse> createNotificationCategoriesResponse(List<NotificationCategory> notificationCategories) {
        var notificationCategoriesResponse = new ArrayList<NotificationCategoryResponse>();
        notificationCategories.forEach(notificationCategory -> {
            notificationCategoriesResponse.add(fromEntity(notificationCategory));
        });

        return notificationCategoriesResponse;
    }
}
