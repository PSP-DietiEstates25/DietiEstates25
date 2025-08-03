package com.dietiestates25.backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates25.backend.repository.UserRepository;
import com.dietiestates25.backend.repository.EstateAgentRepository;
import com.dietiestates25.backend.model.User;
import com.dietiestates25.backend.model.EstateAgent;
import com.dietiestates25.backend.model.Account;
import com.dietiestates25.backend.dto.AuthRequest;
import com.dietiestates25.backend.dto.RegisterRequest;
import com.dietiestates25.backend.dto.AuthResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstateAgentRepository agentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getAccount().getPassword())) {
            throw new RuntimeException("Credenziali errate");
        }

        String token = jwtService.generateToken(request.getEmail());
        return new AuthResponse(token, "USER"); // estendere anche per agent
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        if (request.getUserType().equalsIgnoreCase("agent")) {
            throw new RuntimeException("Gli agenti non possono registrarsi autonomamente");
        }

        Account account = new Account(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        User user = new User();
        user.setAccount(account);
        userRepository.save(user);
    }
}