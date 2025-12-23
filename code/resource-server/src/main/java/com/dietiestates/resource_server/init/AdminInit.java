package com.dietiestates.resource_server.init;

import com.dietiestates.resource_server.config.ResourceServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.repository.AdminRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInit {

    private final ResourceServerProperties properties;
    private final AdminRepository adminRepository;

    @PostConstruct
    public void init() {
        if(adminRepository.findByEmail(properties.superAdminEmail()).isEmpty()) {

            var admin = new Admin();
            admin.setEmail(properties.superAdminEmail());
            admin.setAdmin(admin);

            adminRepository.save(admin);
        }
    }
}