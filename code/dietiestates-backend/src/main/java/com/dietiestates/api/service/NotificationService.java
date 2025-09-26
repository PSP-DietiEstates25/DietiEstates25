package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.NotificationDto;
import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.repository.NotificationCategoryRepository;
import com.dietiestates.api.repository.NotificationRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationCategoryRepository notificationCategoryRepository;
	private final UserRepository userRepository;
	
	public void createNotification(NotificationDto request) {
		var notification = of(request);
		notificationRepository.save(notification);
	}
	
	public Notification of(NotificationDto request) {
		
		var notificationCategory = notificationCategoryRepository.findByName(NotificationCategoryType.valueOf(request.getNotificationCategoryName()))
					.orElseThrow(NotificationCategoryNotFoundException::new);
		
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		return Notification.builder()
				.createdDate(LocalDateTime.now())
				.message(request.getMessage())
				.notificationCategory(notificationCategory)
				.user(user)
				.build();
	}
	/*
	private final NotificationRepository notifRepo;
	private final UserRepository userRepo;
	private final NotificationPreferenceService prefService;

	//crea e consegna una notifica se la categoria è abilitata per l'utente.
	@Transactional
	public NotificationResponse push(String userEmail, NotificationCategoryType category, String title, String message,
			Long adId) {
		if (!prefService.isEnabled(userEmail, category)) {
			// preferenza disattivata -> non creare la notifica
			return null;
		}

		User user = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

		Notification entity = Notification.builder()
				.category(category)
				.title(title)
				.message(message)
				.adId(adId)
				.user(user)
				.readFlag(false)
				.createdAt(Instant.now())
				.build();

		Notification saved = notifRepo.save(entity);

		return NotificationResponse.builder()
				.id(saved.getId())
				.category(saved.getCategory().name())
				.title(saved.getTitle())
				.message(saved.getMessage())
				.adId(saved.getAdId())
				.read(saved.getReadFlag())
				.createdAt(saved.getCreatedAt())
				.build();
	}

	//lista notifiche dell'utente (tutte o solo non-letto) con paginazione.
	@Transactional(readOnly = true)
	public List<NotificationResponse> listMine(String email, boolean unreadOnly, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
		var pageObj = unreadOnly
				? notifRepo.findByUser_EmailAndReadFlagFalseOrderByCreatedAtDesc(email, pageable)
				: notifRepo.findByUser_EmailOrderByCreatedAtDesc(email, pageable);

		return pageObj.map(n -> NotificationResponse.builder()
				.id(n.getId())
				.category(n.getCategory().name())
				.title(n.getTitle())
				.message(n.getMessage())
				.adId(n.getAdId())
				.read(n.getReadFlag())
				.createdAt(n.getCreatedAt())
				.build())
				.getContent();
	}

	//segna una notifica come letta.
	@Transactional
	public void markRead(String email, Long id) {
		Notification n = notifRepo.findById(id)
				.filter(ent -> ent.getUser().getEmail().equals(email))
				.orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
		n.setReadFlag(true);
		notifRepo.save(n);
	}

	//segna tutte come lette (batch).
	@Transactional
	public void markAllRead(String email) {
		int page = 0;
		List<Notification> batch;
		do {
			batch = notifRepo.findByUser_EmailAndReadFlagFalseOrderByCreatedAtDesc(email, PageRequest.of(page, 100))
					.getContent();
			for (var n : batch) {
				n.setReadFlag(true);
			}
			notifRepo.saveAll(batch);
			page++;
		} while (!batch.isEmpty());
	}

	@Transactional
	public void delete(String email, Long id) {
		if (!notifRepo.existsByIdAndUser_Email(id, email)) {
			throw new IllegalArgumentException("Notification not found: " + id);
		}
		notifRepo.deleteByIdAndUser_Email(id, email);
	}

	//contatore non letti.
	@Transactional(readOnly = true)
	public long unreadCount(String email) {
		return notifRepo.countByUser_EmailAndReadFlagFalse(email);
	}

	private int safePage(Integer p) {
		return (p != null && p >= 0) ? p : 0;
	}

	private int safeSize(Integer s) {
		return (s != null && s > 0) ? s : 12;
	}
	*/
}
