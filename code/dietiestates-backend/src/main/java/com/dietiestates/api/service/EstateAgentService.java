package com.dietiestates.api.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RoleRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstateAgentService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EstateAgentRepository estateAgentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAgent(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email già registrata");
        }

        Role agentRole = roleRepository.findByName("AGENT")
                .orElseThrow(() -> new IllegalStateException("Ruolo 'AGENT' mancante"));

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a.getName() == null) {
            throw new IllegalStateException("Utente non autenticato");
        }

        User currentUser = userRepository.findByUsername(a.getName())
                .orElseThrow(() -> new IllegalStateException("Utente corrente non trovato"));

        Admin createdByAdmin = adminRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("Profilo Admin non trovato per l'utente corrente"));

        User agentUser = new User();
        agentUser.setUsername(email); // username = email
        agentUser.setEmail(email);
        agentUser.setPassword(passwordEncoder.encode(rawPassword));
        agentUser.setRoles(List.of(agentRole));
        agentUser = userRepository.save(agentUser);

        EstateAgent agent = new EstateAgent();
        agent.setUser(agentUser);
        agent.setAdmin(createdByAdmin);
        estateAgentRepository.save(agent);
    }
}
