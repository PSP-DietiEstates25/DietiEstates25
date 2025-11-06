package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.model.User;
import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.NotificationCategoryFactory;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.spec.NotificationCategorySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryFactoryDefaultImpl implements NotificationCategoryFactory {

    @Override
    public NotificationCategory createNotificationCategoryFromSpec(
            NotificationCategorySpec spec,
            User user
    ) {
        return NotificationCategory.builder()
                .name(spec.getName())
                .isActive(spec.getIsActive())
                .user(user)
                .build();
    }

}
