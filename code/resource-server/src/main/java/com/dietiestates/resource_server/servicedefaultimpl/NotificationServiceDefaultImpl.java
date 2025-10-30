package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.resource_server.factory.NotificationFactory;
import com.dietiestates.resource_server.finder.NotificationCategoryFinder;
import com.dietiestates.resource_server.finder.NotificationFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.NotificationMapper;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.repository.NotificationRepository;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.spec.NotificationSpec;
import com.dietiestates.resource_server.verifier.NotificationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceDefaultImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationFactory notificationFactory;
	private final NotificationFinder notificationFinder;
	private final NotificationVerifier notificationVerifier;
	private final NotificationMapper notificationMapper;
	
	private final UserFinder userFinder;
	private final NotificationCategoryFinder notificationCategoryFinder;
	
	@Override
	public NotificationResponse createNotification(
			String notificationCategoryName,
			NotificationRequest request
			) {
		
		var notificationSpec = notificationMapper.toSpec(request);
		
		var notificationCategory = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName.toUpperCase());
		var user = userFinder.getUserByEmail(notificationSpec.getUserEmail());
	
		var notification = notificationFactory.createNotificationFromSpec(notificationSpec, notificationCategory, user);
		notificationRepository.save(notification);
		
		return notificationMapper.fromEntity(notification);
	}

    @Override
    public void createNotificationsAfterRealEstateCreation(List<Search> searchesToNotify){

        searchesToNotify.forEach(search -> {

            var newPropertiesNotificationCategory = notificationCategoryFinder.getNotificationCategoryByName("NEW_PROPERTIES");
            var notificationSpec = NotificationSpec.builder()
                    .message("New property available")
                    .userEmail(search.getUser().getEmail())
                    .build();
            var notification = notificationFactory.createNotificationFromSpec(
                    notificationSpec,
                    newPropertiesNotificationCategory,
                    search.getUser()
            );

            notificationRepository.save(notification);
        });
    }
	
	@Override
	public NotificationResponse getNotificationById(
			String notificationCategoryName,
			Long notificationId
    ) throws NotificationNotOwnedByNotificationCategoryException {

        notificationVerifier.checkNotificationOwnedByNotificationCategory(notificationId, notificationCategoryName);
		var notification = notificationFinder.getNotificationById(notificationId);

		return notificationMapper.fromEntity(notification);
	}

    @Override
    public List<NotificationResponse> getPrincipalNotifications(
            Principal principal,
            String notificationCategoryName
    ) {

        var user = userFinder.getUserByEmail(principal.getName());
        var notificationCategory = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName);
        var notifications = notificationFinder.getPrincipalNotifications(user, notificationCategory);

        return notificationMapper.createNotificationsResponse(notifications);
    }
}
