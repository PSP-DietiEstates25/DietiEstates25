package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long>, PagingAndSortingRepository<Notification, Long> {
    boolean existsById(Long id);
    boolean existsByIdAndNotificationCategoryName(Long id, NotificationCategory notificationCategoryName);
    Page<Notification> findByNegotiationId(Long id, Pageable pageable);
}
