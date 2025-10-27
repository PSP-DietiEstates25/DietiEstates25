package com.dietiestates.auth.service;

import com.dietiestates.auth.finder.DefaultAccountFinder;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.auth.model.SecurityAccount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DefaultAccountFinder defaultAccountFinder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var account = defaultAccountFinder.getDefaultAccountByEmail(email);

        return SecurityAccount.builder()
                .defaultAccount(account)
                .enabled(true)
                .locked(false)
                .build();
    }
}