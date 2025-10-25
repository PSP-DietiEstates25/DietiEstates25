package com.dietiestates.auth.config;

import org.springframework.stereotype.Component;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.Role;
import com.dietiestates.auth.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleConfig {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if(roleRepository.findByName(RoleName.USER).isEmpty()) {
            roleRepository.save(
                    Role.builder().name("USER").build()
            );
        }

        if(roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            roleRepository.save(
                    Role.builder().name("ADMIN").build()
            );
        }

        if(roleRepository.findByName(RoleName.ESTATE_AGENT).isEmpty()) {
            roleRepository.save(
                    Role.builder().name("ESTATE_AGENT").build()
            );
        }
    }
}