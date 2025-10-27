package com.dietiestates.resource_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.service.NotificationCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificationcategories")
@RequiredArgsConstructor
public class NotificationCategoryController {

    private final NotificationCategoryService notificationCategoryService;

    @PostMapping
    public ResponseEntity<NotificationCategoryResponse> createNotificationCategory(
            @RequestBody NotificationCategoryRequest request
    ){
        var notificationCategory = notificationCategoryService.createNotificationCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationCategory);
    }

    @GetMapping("/{notificationcategoryname}")
    public ResponseEntity<NotificationCategoryResponse> getNotificationCategoryByName(
            @PathVariable String notificationcategoryname
    ){
        var notificationCategory = notificationCategoryService.getNotificationCategoryByName(notificationcategoryname);
        return ResponseEntity.status(HttpStatus.OK).body(notificationCategory);
    }
}
