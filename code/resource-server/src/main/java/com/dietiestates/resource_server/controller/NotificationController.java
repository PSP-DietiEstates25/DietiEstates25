package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.service.NotificationService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notificationcategories/{notificationcategoryname}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @PathVariable String notificationcategoryname,
            @RequestBody NotificationRequest request
    ){

        var notification = notificationService.createNotification(notificationcategoryname, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/{notificationid}")
    public ResponseEntity<NotificationResponse> getNotificationById(
            @PathVariable String notificationcategoryname,
            @PathVariable Long notificationid
    ) {
        var notification = notificationService.getNotificationById(notificationcategoryname, notificationid);
        return ResponseEntity.status(HttpStatus.OK).body(notification);
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotificationCategoryNotifications(
            @PathVariable String notificationcategoryname,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ){
        var notifications = notificationService.getNotificationCategoryNotifications(notificationcategoryname, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
}

