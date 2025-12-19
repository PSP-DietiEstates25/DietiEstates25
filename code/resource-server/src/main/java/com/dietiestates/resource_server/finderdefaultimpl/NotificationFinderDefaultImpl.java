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
import java.util.Comparator;
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
    public Page<Notification> getUserNotifications(Long userId, List<String> notificationCategories, Pageable pageable) {
        List<Negotiation> allUserNegotiations = negotiationFinder.getAllUserNegotiations(userId);
        List<Notification> allUserNotifications = extractAllNegotiationsNotifications(allUserNegotiations, notificationCategories);
        allUserNotifications.sort(Comparator.comparing(Notification::getCreatedDate).reversed());
        return PageUtils.toPage(allUserNotifications, pageable);
    }

    @Override
    public Page<Notification> getNegotiationNotifications(Long negotiationId, Pageable pageable) {
        return notificationRepository.findByNegotiationId(negotiationId, pageable);
    }

    @Override
    public List<Notification> extractAllNegotiationsNotifications(List<Negotiation> negotiations, List<String> notificationCategories) {
        var notifications = new ArrayList<Notification>();

        List<NotificationCategory> targetCategories = new ArrayList<>();

        if (notificationCategories != null && !notificationCategories.isEmpty()) {
            for (String catStr : notificationCategories) {
                if (NotificationUtils.checkNotificationCategoryExists(catStr)) {
                    targetCategories.add(NotificationUtils.extractNotificationCategory(catStr));
                }
            }
        }

        negotiations.forEach(negotiation -> {
            var negotiationNotifications = negotiation.getNotifications();

            if (negotiationNotifications == null) return;

            negotiationNotifications.forEach(notification -> {
                if (targetCategories.isEmpty() || targetCategories.contains(notification.getNotificationCategory())) {
                    notifications.add(notification);
                }
            });
        });

        return notifications;
    }
}
