package com.dietiestates.api.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.api.factory.NotificationFactory;
import com.dietiestates.api.finder.NotificationCategoryFinder;
import com.dietiestates.api.finder.NotificationFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.mapper.NotificationMapper;
import com.dietiestates.api.repository.DefaultAccountRepository;
import com.dietiestates.api.repository.NotificationRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.service.NotificationService;
import com.dietiestates.api.verifier.NotificationVerifier;
import com.dietiestates.api.enums.NotificationCategoryType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationFactory notificationFactory;
	private final NotificationFinder notificationFinder;
	private final NotificationVerifier notificationVerifier;
	private final NotificationMapper notificationMapper;

	private final DefaultAccountRepository defaultAccountRepository;
	private final UserRepository userRepository;
	private final UserFinder userFinder;
	private final NotificationCategoryFinder notificationCategoryFinder;

	@Override
	public NotificationResponse createNotification(
			String notificationCategoryName,
			NotificationRequest request) {

		var notificationSpec = notificationMapper.toSpec(request);

		var notificationCategory = notificationCategoryFinder
				.getNotificationCategoryByName(notificationCategoryName.toUpperCase());
		var user = userFinder.getUserByEmail(notificationSpec.getUserEmail());

		var notification = notificationFactory.createNotificationFromSpec(notificationSpec, notificationCategory, user);
		notificationRepository.save(notification);

		return notificationMapper.fromEntity(notification);
	}

	@Override
	public NotificationResponse getNotificationById(
			String notificationCategoryName,
			Long notificationId)
			throws NotificationNotOwnedByNotificationCategoryException {
		var notificationCategory = notificationCategoryFinder.getNotificationCategoryByName(notificationCategoryName);
		var notification = notificationFinder.getNotificationById(notificationId);

		notificationVerifier.checkNotificationOwnedByNotificationCategory(
				notification.getNotificationCategory().getId(), notificationCategory.getId());

		return notificationMapper.fromEntity(notification);
	}

	@Override
	public Page<NotificationResponse> listMyNotifications(String principalName, int page, int size) {
		var pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));

		var account = defaultAccountRepository.findByEmail(principalName)
				.or(() -> defaultAccountRepository.findByEmail(principalName))
				.orElseThrow(() -> new IllegalArgumentException("Account not found for principal: " + principalName));

		var user = userRepository.findBySecurityAccountDecorator_Id(account.getId())
				.orElseThrow(() -> new IllegalStateException("User not found for accountId: " + account.getId()));

		var pageEntities = notificationRepository.findByUser_IdOrderByCreatedDateDesc(user.getId(), pageable);
		return pageEntities.map(notificationMapper::fromEntity);
	}

	@Override
	public Page<NotificationResponse> listMyNotifications(
			String principalName,
			String notificationCategoryName,
			int page,
			int size) {
		var pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));

		var account = defaultAccountRepository.findByEmail(principalName)
				.orElseThrow(() -> new IllegalArgumentException("Account not found for principal: " + principalName));

		var user = userRepository.findBySecurityAccountDecorator_Id(account.getId())
				.orElseThrow(() -> new IllegalStateException("User not found for accountId: " + account.getId()));

		var cat = NotificationCategoryType.valueOf(notificationCategoryName.toUpperCase());
		var pageEntities = notificationRepository
				.findByUser_IdAndNotificationCategory_NameOrderByCreatedDateDesc(user.getId(), cat, pageable);

		return pageEntities.map(notificationMapper::fromEntity);
	}
}
