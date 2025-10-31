package com.dietiestates.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificationcategories/{notificationcategoryname}/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@PostMapping
	public ResponseEntity<NotificationResponse> createNotification(
			@PathVariable String notificationcategoryname,
			@RequestBody NotificationRequest request) {
		var notification = notificationService.createNotification(notificationcategoryname, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(notification);
	}

	@GetMapping("/{notificationid}")
	public ResponseEntity<NotificationResponse> getNotificationById(
			@PathVariable String notificationcategoryname,
			@PathVariable Long notificationid) {
		var notification = notificationService.getNotificationById(notificationcategoryname, notificationid);
		return ResponseEntity.status(HttpStatus.OK).body(notification);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<NotificationResponse>> listMine(
			Authentication authentication,
			@PathVariable("notificationcategoryname") String notificationcategoryname,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size) {

		var principalName = authentication.getName();
		var res = notificationService.listMyNotifications(principalName, notificationcategoryname, page, size);
		return ResponseEntity.ok(res);
	}
}
