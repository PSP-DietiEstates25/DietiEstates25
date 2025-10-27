package com.dietiestates.resource_server.factorydefaultimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.RoleFactory;
import com.dietiestates.resource_server.model.Role;

@Component
@RequiredArgsConstructor
public class RoleFactoryDefaultImpl implements RoleFactory {

    @Override
    public Role createRoleFromSpec(String name) {
        return Role.builder()
                .name(name)
                .build();
    }

}
