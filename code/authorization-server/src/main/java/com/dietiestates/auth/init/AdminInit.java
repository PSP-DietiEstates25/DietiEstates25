package com.dietiestates.auth.init;

import com.dietiestates.auth.config.AuthorizationServerProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.model.SecurityAccount;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import com.dietiestates.auth.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@DependsOn("roleInit")
@RequiredArgsConstructor
public class AdminInit {

    private final AuthorizationServerProperties properties;

    private final RoleRepository roleRepository;
    private final DefaultAccountRepository defaultAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if(defaultAccountRepository.findByEmail(properties.superAdminEmail()).isEmpty()) {

            var defaultAccount = DefaultAccount.builder()
                    .email(properties.superAdminEmail())
                    .password(passwordEncoder.encode(properties.superAdminPassword()))
                    .role(roleRepository.findByName(RoleName.ADMIN).get())
                    .build();

            defaultAccountRepository.save(defaultAccount);
        }
    }
}