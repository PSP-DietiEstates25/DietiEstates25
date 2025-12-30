package com.dietiestates.auth.service;

import javax.management.relation.RoleNotFoundException;

import com.dietiestates.auth.factory.SecurityAccountFactory;
import com.dietiestates.auth.finder.DefaultAccountFinder;
import com.dietiestates.auth.mapper.AuthMapper;
import com.dietiestates.auth.verifier.DefaultAccountVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.auth.dto.request.AuthRequest;
import com.dietiestates.auth.dto.response.AuthResponse;
import com.dietiestates.auth.factory.DefaultAccountFactory;
import com.dietiestates.auth.finder.RoleFinder;
import com.dietiestates.auth.repository.DefaultAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DefaultAccountFactory defaultAccountFactory;
    private final SecurityAccountFactory securityAccountFactory;
    private final DefaultAccountRepository defaultAccountRepository;
    private final DefaultAccountFinder defaultAccountFinder;

    private final RoleFinder roleFinder;
    private final AuthMapper authMapper;
    private final DefaultAccountVerifier defaultAccountVerifier;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(AuthRequest request) throws RoleNotFoundException {

        defaultAccountVerifier.checkDefaultAccountDoesntExists(request.getEmail());

        var authenticationSpec = authMapper.toSpec(request);

        var accountRole = roleFinder.getByRoleName(request.getRole());
        var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, accountRole);
        var securityAccountDecorator = securityAccountFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);

        defaultAccountRepository.save(defaultAccount);
        return authMapper.fromEntity(securityAccountDecorator);
    }

}
