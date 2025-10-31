package com.dietiestates.auth.factory;

import org.springframework.stereotype.Component;

import com.dietiestates.auth.model.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleFactory {

    public Role createRoleFromSpec(String name) {
        return Role.builder()
                .name(name)
                .build();
    }
}