package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.EstateAgentFactory;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentFactoryDefaultImpl implements EstateAgentFactory {

    @Override
    public EstateAgent createEstateAgentFromSpec(
            String email,
            Admin admin
    ) {
        return EstateAgent.builder()
                .email(email)
                .admin(admin)
                .build();
    }

}
