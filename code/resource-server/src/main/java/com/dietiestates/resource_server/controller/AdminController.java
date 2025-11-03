package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.AdminResponse;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> registerAdmin(
            @RequestBody StafferRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) throws RoleNotFoundException {

        var creatorEmail = jwt.getSubject();

        var admin = adminService.register(request, creatorEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(admin);
    }

    @GetMapping("/{adminid}")
    public ResponseEntity<AdminResponse> getAdminById(
            @PathVariable Long adminid
    ) throws AdminNotFoundException {

        var admin = adminService.getAdminById(adminid);
        return ResponseEntity.status(HttpStatus.OK).body(admin);
    }

}
