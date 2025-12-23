package com.dietiestates.auth.service;

import javax.management.relation.RoleNotFoundException;

import com.dietiestates.auth.factory.SecurityAccountFactory;
import com.dietiestates.auth.mapper.RegisterMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.auth.dto.request.RegisterRequest;
import com.dietiestates.auth.dto.response.RegisterResponse;
import com.dietiestates.auth.factory.DefaultAccountFactory;
import com.dietiestates.auth.finder.RoleFinder;
import com.dietiestates.auth.repository.DefaultAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final DefaultAccountFactory defaultAccountFactory;
    private final SecurityAccountFactory securityAccountFactory;
    private final DefaultAccountRepository defaultAccountRepository;

    private final RoleFinder roleFinder;
    private final RegisterMapper registerMapper;

    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) throws RoleNotFoundException {

        var authenticationSpec = registerMapper.toSpec(request);

        var accountRole = roleFinder.getByRoleName(request.getRole());
        var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, accountRole);
        var securityAccountDecorator = securityAccountFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);

        defaultAccountRepository.save(defaultAccount);
        return registerMapper.fromEntity(securityAccountDecorator);
    }
}
