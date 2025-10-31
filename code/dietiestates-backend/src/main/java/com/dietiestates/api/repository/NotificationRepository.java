package com.dietiestates.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.dietiestates.api.enums.NotificationCategoryType;

import com.dietiestates.api.model.Notification;

public interface NotificationRepository extends
        CrudRepository<Notification, Long>,
        PagingAndSortingRepository<Notification, Long> {

    Page<Notification> findByUser_IdOrderByCreatedDateDesc(Long userId, Pageable pageable);

    Page<Notification> findByUser_IdAndNotificationCategory_NameOrderByCreatedDateDesc(
            Long userId,
            NotificationCategoryType name,
            Pageable pageable);

}
