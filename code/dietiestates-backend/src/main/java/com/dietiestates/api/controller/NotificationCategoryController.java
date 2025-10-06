package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.service.NotificationCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificationcategories")
@RequiredArgsConstructor
public class NotificationCategoryController {

	private final NotificationCategoryService notificationCategoryService;
	
	@PostMapping
	public ResponseEntity<?> createNotificationCategory(
			@RequestBody NotificationCategoryRequest request
			){
		notificationCategoryService.createNotificationCategory(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
