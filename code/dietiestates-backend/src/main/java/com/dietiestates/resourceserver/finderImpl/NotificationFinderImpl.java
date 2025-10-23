package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resourceserver.finder.NotificationFinder;
import com.dietiestates.resourceserver.model.Notification;
import com.dietiestates.resourceserver.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFinderImpl implements NotificationFinder {

	private final NotificationRepository notificationRepository;

	@Override
	public Notification getNotificationById(Long id)
			throws NotificationNotFoundException {
		return notificationRepository.findById(id)
				.orElseThrow(NotificationNotFoundException::new);
	}
	
}
