package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.dietiestates.resource_server.factory.UserFactory;
import com.dietiestates.resource_server.model.User;
import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class UserFactoryDefaultImpl implements UserFactory {

    @Override
    public User createUserFromSpec(
            String email
    ) {
        return User.builder()
                .email(email)
                .build();
    }

}
