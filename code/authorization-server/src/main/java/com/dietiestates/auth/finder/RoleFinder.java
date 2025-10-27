package com.dietiestates.auth.finder;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.Role;
import com.dietiestates.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleFinder {

    private final RoleRepository roleRepository;

    public Role getByRoleName(String roleName)
            throws RoleNotFoundException {
        return roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(RoleNotFoundException::new);
    }
}