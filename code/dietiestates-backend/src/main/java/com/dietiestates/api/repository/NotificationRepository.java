package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

}
