package com.dietiestates.auth.federation;

import java.util.UUID;
import java.util.function.Consumer;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import com.dietiestates.auth.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryOAuth2UserHandler implements Consumer<OAuth2User> {

    private final DefaultAccountRepository defaultAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void accept(OAuth2User user) {
        if (this.defaultAccountRepository.findByEmail(user.getAttribute("email")).isEmpty()) {
            
            System.out.println("Saving first-time user: name=" + user.getName() + ", claims=" + user.getAttributes() + ", authorities=" + user.getAuthorities());

            String randomPsw = passwordEncoder.encode(UUID.randomUUID().toString());

            var userRole = roleRepository.findByName(RoleName.USER);
            var defaultAccount = DefaultAccount.builder()
                    .email(user.getAttribute("email"))
                    .password(randomPsw)
                    .role(userRole.get())
                    .build();

            this.defaultAccountRepository.save(defaultAccount);
        }
    }

    private String resolveProviderId(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2AuthenticationToken token) {
            return token.getAuthorizedClientRegistrationId();
        }
        return null;
    }
}

