package com.dietiestates.api.service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.LoginRequest;
import com.dietiestates.api.dto.LoginResponse;
import com.dietiestates.api.dto.RegisterRequest;
import com.dietiestates.api.model.Staffer;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.StafferRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

@Service
public class AuthService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private StafferRepository stafferRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtService jwt;

    public LoginResponse login(LoginRequest req) {
        String email = req.email();
        String raw = req.password();

        // Staffer
        Optional<Staffer> st = stafferRepository.findById(email);
        if (st.isPresent()) {
            Staffer staffer = st.get();
            if(!passwordEncoder.matches(raw, staffer.getPassword())){   // confronto con la password codificata
                throw new IllegalArgumentException("Credenziali non valide");
            }

            String role = getRoleFromType(staffer);       // admin, agent
            Map<String, Object> claims = new HashMap<>(); // claims per il token JWT (<email, role, subjectType>)
            claims.put("role", role);
            claims.put("subjectType", "STAFFER");
            String token = jwt.generate(email, claims);
            return new LoginResponse(token, role, "STAFFER");
        }

        // User
        Optional<User> us = userRepository.findById(email);
        if(us.isPresent()) {
            User user = us.get();
            if(!passwordEncoder.matches(raw, user.getPassword())) {
                throw new IllegalArgumentException("Credenziali non valide");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "user");
            claims.put("subjectType", "USER");
            String token = jwt.generate(email, claims);
            return new LoginResponse(token, "user", "USER");
        }

        throw new IllegalArgumentException("Utente non trovato");
    }

    public void register(RegisterRequest req) {
        if(userRepository.existsById(req.email())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        User us = new User();
        us.setEmail(req.email());
        us.setPassword(passwordEncoder.encode(req.password())); // BCrypt
        userRepository.save(us);
    }

    private String getRoleFromType(Staffer st) {
        // deriva dal tipo runtime (SINGLE_TABLE con discriminator "role")
        return switch (st.getClass().getSimpleName()) {
            case "Admin" -> "admin";
            case "EstateAgent" -> "estate_agent";
            default -> "staffer";
        };
    }
}
