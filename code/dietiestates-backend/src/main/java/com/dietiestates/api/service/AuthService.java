package com.dietiestates.api.service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private final UserRepository userRepo;
    private final StafferRepository stafferRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepo, StafferRepository stafferRepo, PasswordEncoder encoder, JwtService jwt) {
        this.userRepo = userRepo;
        this.stafferRepo = stafferRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public LoginResponse login(LoginRequest req) {
        String email = req.email();
        String raw = req.password();

        // Staffer
        Optional<Staffer> st = stafferRepo.findById(email);
        if (st.isPresent()) {
            Staffer staffer = st.get();
            if(!encoder.matches(raw, staffer.getPassword())){   // confronto con la password codificata
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
        Optional<User> us = userRepo.findById(email);
        if(us.isPresent()) {
            User user = us.get();
            if(!encoder.matches(raw, user.getPassword())) {
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
        if(userRepo.existsById(req.email())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        User us = new User();
        us.setEmail(req.email());
        us.setPassword(encoder.encode(req.password())); // BCrypt
        userRepo.save(us);
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
