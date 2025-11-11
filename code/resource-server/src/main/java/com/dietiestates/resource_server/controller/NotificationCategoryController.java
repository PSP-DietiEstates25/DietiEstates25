package com.dietiestates.resource_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<NotificationCategoryResponse>> getUserNotificationCategories(
            @RequestParam String email
    ){
        return null;
    }

    @PutMapping("/{notificationcategoryname}")
    public ResponseEntity<NotificationCategoryResponse> updateIsActive(
            @PathVariable String notificationcategoryname,
            @RequestBody NotificationCategoryRequest request
    ) {

        notificationCategoryService.updateNotificationCategory(notificationcategoryname, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
