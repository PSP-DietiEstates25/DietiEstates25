package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.finder.NegotiationFinder;
import com.dietiestates.resource_server.finder.NotificationFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.repository.NotificationRepository;
import com.dietiestates.resource_server.utils.NotificationUtils;
import com.dietiestates.resource_server.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFinderDefaultImpl implements NotificationFinder {

	private final NotificationRepository notificationRepository;

    private final NegotiationFinder negotiationFinder;

	@Override
	public Notification getNotificationById(Long id)
			throws NotificationNotFoundException {
		return notificationRepository.findById(id)
				.orElseThrow(NotificationNotFoundException::new);
	}

    @Override
    public Page<Notification> getUserNotifications(Long userId, String notificationCategory, Pageable pageable) {
        List<Negotiation> allUserNegotiations = negotiationFinder.getAllUserNegotiations(userId);
        List<Notification> allUserNotificationsByCategory = extractAllNegotiationsNotifications(allUserNegotiations, notificationCategory);
        return PageUtils.toPage(allUserNotificationsByCategory, pageable);
    }

    @Override
    public Page<Notification> getNegotiationNotifications(Long negotiationId, Pageable pageable) {
        return notificationRepository.findByNegotiationId(negotiationId, pageable);
    }

    @Override
    public List<Notification> extractAllNegotiationsNotifications(List<Negotiation> negotiations, String notificationCategory){
        var notifications = new ArrayList<Notification>();
        NotificationCategory requestedNotificationCategory = null;

        if(NotificationUtils.checkNotificationCategoryExists(notificationCategory)){
            requestedNotificationCategory = NotificationUtils.extractNotificationCategory(notificationCategory);
        }

        var targetNotificationCategory = requestedNotificationCategory;

        negotiations.forEach(negotiation -> {
            var negotiationNotifications = negotiation.getNotifications();
            if(targetNotificationCategory != null){
                negotiationNotifications.forEach(notification -> {
                    if(targetNotificationCategory.equals(notification.getNotificationCategory()))
                        notifications.add(notification);
                });
            } else {
                notifications.addAll(negotiationNotifications);
            }
        });
        return notifications;
    }
}
