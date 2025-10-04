package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.NotificationNotFoundException;
import com.dietiestates.api.finder.NotificationFinder;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.repository.NotificationRepository;

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
