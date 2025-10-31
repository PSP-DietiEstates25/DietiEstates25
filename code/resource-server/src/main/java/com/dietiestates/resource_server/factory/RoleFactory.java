package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Role;

public interface RoleFactory {

    Role createRoleFromSpec(
            String name
    );
}
