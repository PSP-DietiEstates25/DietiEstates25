package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.NotificationCategory;
import org.springframework.data.repository.CrudRepository;

public interface NotificationPreferenceRepository extends CrudRepository</*NotificationPreference*/NotificationCategory, Long> {

    boolean existsById(Long id);

    //Optional<NotificationPreference> findByUser_EmailAndCategory(String email, NotificationCategoryType category);

    //List<NotificationPreference> findAllByUser_Email(String email);
}
