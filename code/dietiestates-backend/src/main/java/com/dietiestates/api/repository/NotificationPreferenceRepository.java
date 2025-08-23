package com.dietiestates.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationPreference;

public interface NotificationPreferenceRepository extends CrudRepository<NotificationPreference, Long> {

    Optional<NotificationPreference> findByUser_EmailAndCategory(String email, NotificationCategoryType category);

    List<NotificationPreference> findAllByUser_Email(String email);
}
