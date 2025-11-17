package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.dto.request.UpdateNotificationCategoryStatusRequest;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.service.NotificationCategoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<NotificationCategoryResponse>> getUserNotificationCategories(
            @AuthenticationPrincipal Jwt jwt
    ){
        var userEmail = jwt.getSubject();

        var notificationCategories = notificationCategoryService.getUserNotificationCategories(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(notificationCategories);
    }

    @PutMapping("/{notificationcategoryname}")
    public ResponseEntity<NotificationCategoryResponse> updateIsActive(
            @PathVariable String notificationcategoryname,
            @RequestBody UpdateNotificationCategoryStatusRequest request
    ) {

        notificationCategoryService.updateNotificationCategory(notificationcategoryname, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
