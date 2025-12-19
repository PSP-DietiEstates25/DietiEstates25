package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByUserException;
import com.dietiestates.resource_server.factory.NotificationFactory;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.NotificationFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.NotificationMapper;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.repository.NotificationRepository;
import com.dietiestates.resource_server.service.NotificationService;
import com.dietiestates.resource_server.spec.NotificationSpec;
import com.dietiestates.resource_server.verifier.NotificationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceDefaultImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationFactory notificationFactory;
	private final NotificationFinder notificationFinder;
	private final NotificationVerifier notificationVerifier;
	private final NotificationMapper notificationMapper;

    private final NegotiationFinder negotiationFinder;
    private final UserFinder userFinder;
	
	@Override
	public NotificationResponse createNotification(
			NotificationRequest request
    ) {
		var notificationSpec = notificationMapper.toSpec(request);
		var negotiation = negotiationFinder.getNegotiationById(notificationSpec.getNegotiationId());
	
		var notification = notificationFactory.createNotificationFromSpec(notificationSpec, negotiation);
		notificationRepository.save(notification);
		
		return notificationMapper.fromEntity(notification);
	}

    @Override
    public void createNotificationsAfterRealEstateCreation(List<Search> searchesToNotify){

        searchesToNotify.forEach(search -> {

            var user = search.getUser();
            var userNegotiations = user.getNegotiations();

            userNegotiations.forEach(negotiation -> {

                var notificationSpec = NotificationSpec.builder()
                        .message("Un nuovo annuncio è disponibile per la ricerca " + search.getId())
                        .notificationCategory(NotificationCategory.NEW_PROPERTIES.toString())
                        .isVisible(true)
                        .negotiationId(negotiation.getId())
                        .build();

                var notification = notificationFactory.createNotificationFromSpec(notificationSpec, negotiation);
                notificationRepository.save(notification);
            });
        });
    }

	@Override
	public NotificationResponse getNotificationById(
			Long notificationId,
            String userEmail
    ) throws NotificationNotOwnedByUserException {

        //notificationVerifier.checkNotificationOwnedByUser(notificationId, userEmail);
		var notification = notificationFinder.getNotificationById(notificationId);

		return notificationMapper.fromEntity(notification);
	}

    @Override
    public Page<NotificationResponse> getUserNotifications(String userEmail, List<String> notificationCategories, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var user = userFinder.getUserByEmail(userEmail);
        var notifications = notificationFinder.getUserNotifications(user.getId(), notificationCategories, pageable);
        return notificationMapper.createPagedNotificationsResponse(notifications);
    }

    @Override
    public Page<NotificationResponse> getNegotiationNotifications(Long negotationId, Integer page, Integer size) {

        var negotiation = negotiationFinder.getNegotiationById(negotationId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        var notifications = notificationFinder.getNegotiationNotifications(negotiation.getId(), pageable);

        return notificationMapper.createPagedNotificationsResponse(notifications);
    }


}
