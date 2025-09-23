package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.NotificationDto;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	
	@PostMapping
	public ResponseEntity<Notification> createNotification(
			@RequestBody NotificationDto request
			){
		notificationService.createNotification(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//private final NotificationService notifService;
	//private final NotificationPreferenceService prefService;

	/*
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public List<NotificationResponse> listMine(
			Authentication auth,
			@RequestParam(defaultValue = "false") boolean unreadOnly,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "12") Integer size) {
		return notifService.listMine(auth.getName(), unreadOnly, page, size);
	}

	@GetMapping("/unread-count")
	@PreAuthorize("isAuthenticated()")
	public long unreadCount(Authentication auth) {
		return notifService.unreadCount(auth.getName());
	}

	@PatchMapping("/{id}/read")
	@PreAuthorize("isAuthenticated()")
	public void markRead(Authentication auth, @PathVariable Long id) {
		notifService.markRead(auth.getName(), id);
	}

	@PatchMapping("/read-all")
	@PreAuthorize("isAuthenticated()")
	public void markAllRead(Authentication auth) {
		notifService.markAllRead(auth.getName());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public void delete(Authentication auth, @PathVariable Long id) {
		notifService.delete(auth.getName(), id);
	}

	@GetMapping("/preferences")
	@PreAuthorize("isAuthenticated()")
	public List<NotificationPreferenceResponse> listPreferences(Authentication auth) {
		return prefService.listMine(auth.getName());
	}

	@PatchMapping("/preferences/{category}")
	@PreAuthorize("isAuthenticated()")
	public NotificationPreferenceResponse toggle(
			Authentication auth,
			@PathVariable String category,
			@RequestParam boolean enabled) {
		NotificationCategoryType cat = NotificationCategoryType.valueOf(category.toUpperCase());
		return prefService.set(auth.getName(), cat, enabled);
	}

	@PutMapping("/preferences")
	@PreAuthorize("isAuthenticated()")
	public List<NotificationPreferenceResponse> bulk(
			Authentication auth,
			@Valid @RequestBody UpdateNotificationPreferencesRequest req) {
		return prefService.bulkUpdate(auth.getName(), req);
	}
	*/
}
