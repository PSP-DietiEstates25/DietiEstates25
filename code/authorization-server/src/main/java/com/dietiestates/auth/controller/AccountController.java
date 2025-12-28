package com.dietiestates.auth.controller;

import com.dietiestates.auth.dto.request.ChangePasswordRequest;
import com.dietiestates.auth.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            Principal principal
    ) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        accountService.changeOwnPassword(principal, req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<Boolean> checkDefaultAccountExists(
            @PathVariable String email
    ){
        var defaultAccountExists = accountService.checkDefaultAccountExists(email);
        return ResponseEntity.status(HttpStatus.OK).body(defaultAccountExists);
    }
}
