package com.dietiestates.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.StafferRepository;

@Service
public class EstateAgentService {
	
	@Autowired
    private StafferRepository stafferRepository;
	
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void createAgent(String email, String rawPassword) {
        if(stafferRepository.existsById(email)) {
            throw new IllegalArgumentException("Email già registrata");
        }

        EstateAgent agent = new EstateAgent();
        agent.setEmail(email);
        agent.setPassword(passwordEncoder.encode(rawPassword));
        stafferRepository.save(agent);
    }
}