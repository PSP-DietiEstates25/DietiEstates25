package com.dietiestates.auth.init;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${superAdminEmail}")
    private String superAdminEmail;

    @Value("${superAdminPassword}")
    private String superAdminPassword;

    private final RoleRepository roleRepository;
    private final DefaultAccountRepository defaultAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if(defaultAccountRepository.findByEmail(superAdminEmail).isEmpty()) {

            var defaultAccount = DefaultAccount.builder()
                    .email(superAdminEmail)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .role(roleRepository.findByName(RoleName.ADMIN).get())
                    .build();
            var securityAccountDecorator = SecurityAccount.builder()
                    .defaultAccount(defaultAccount)
                    .enabled(true)
                    .locked(false)
                    .build();

            defaultAccountRepository.save(defaultAccount);
        }
    }
}