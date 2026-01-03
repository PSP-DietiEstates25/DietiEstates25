package com.dietiestates.auth.controller;

import com.dietiestates.auth.dto.request.ChangePasswordRequest;
import com.dietiestates.auth.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PatchMapping("/password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordRequest req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var email = jwt.getSubject();

        accountService.changeOwnPassword(email, req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
