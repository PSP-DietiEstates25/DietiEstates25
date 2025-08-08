package com.dietiestates.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.service.NotificationCategoryService;

@RestController
@RequestMapping(value = "/notifications/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationCategoryController {

	@Autowired
	private NotificationCategoryService notificationCategoryService;
	
	@PostMapping
	public NotificationCategory addNewNotificaitonCategory(@RequestBody String name){
			return notificationCategoryService.createNotificationCategory(name);
	}
	
}
