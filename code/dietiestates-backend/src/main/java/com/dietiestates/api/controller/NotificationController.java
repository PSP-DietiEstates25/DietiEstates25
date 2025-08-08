package com.dietiestates.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.model.Notification;
import com.dietiestates.api.service.NotificationService;

@RestController
@RequestMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@PostMapping
	public Notification addNewNotification(@RequestBody String message, @RequestBody String notificationCategory) {
		return notificationService.createNotification(message, notificationCategory);
	}
}
