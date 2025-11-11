package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.User;

public interface UserFactory {
    User createUserFromSpec(String email);
}
