package com.dietiestates.resource_server.init;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.model.Role;
import com.dietiestates.resource_server.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolesInit {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(
                    Role.builder().name("ROLE_USER").build()
            );
        }

        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(
                    Role.builder().name("ROLE_ADMIN").build()
            );
        }

        if(roleRepository.findByName("ROLE_ESTATE_AGENT").isEmpty()) {
            roleRepository.save(
                    Role.builder().name("ROLE_ESTATE_AGENT").build()
            );
        }
    }
}
