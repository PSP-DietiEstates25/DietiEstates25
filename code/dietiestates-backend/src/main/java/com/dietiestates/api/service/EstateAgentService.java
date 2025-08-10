package com.dietiestates.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.StafferRepository;

@Service
public class EstateAgentService {
    private final StafferRepository stafferRepository;
    private final PasswordEncoder encoder;

    public EstateAgentService(StafferRepository stafferRepository, PasswordEncoder encoder) {
        this.stafferRepository = stafferRepository;
        this.encoder = encoder;
    }
    
    public void createAgent(String email, String rawPassword) {
        if(stafferRepository.existsById(email)) {
            throw new IllegalArgumentException("Email già registrata");
        }

        EstateAgent agent = new EstateAgent();
        agent.setEmail(email);
        agent.setPassword(encoder.encode(rawPassword));
        stafferRepository.save(agent);
    }
}