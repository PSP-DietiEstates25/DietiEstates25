package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.enums.Role;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/realestates")
@RequiredArgsConstructor
public class RealEstateController {

        private final RealEstateService realEstateSerivce;

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<RealEstateResponse> createRealEstate(
                        @RequestPart("data") @Valid RealEstateRequest request,
                        @RequestPart(value = "images", required = false) List<MultipartFile> images,
                        @AuthenticationPrincipal Jwt jwt) throws IOException {

                var estateAgentEmail = jwt.getSubject();

                var realEstate = realEstateSerivce.createRealEstate(request, images, estateAgentEmail);
                return ResponseEntity.status(HttpStatus.CREATED).body(realEstate);
        }

        @GetMapping("/{realestateid}")
        public ResponseEntity<RealEstateResponse> getRealEstateById(
                        @PathVariable Long realestateid) {
                var realEstate = realEstateSerivce.getRealEstateById(realestateid);
                return ResponseEntity.status(HttpStatus.OK).body(realEstate);
        }

        @GetMapping
        public ResponseEntity<Page<RealEstateResponse>> getRealEstates(
                        @RequestParam(required = false, defaultValue = "0") Integer page,
                        @RequestParam(required = false, defaultValue = "5") Integer size,
                        @RequestParam(required = false) Long searchid,
                        @AuthenticationPrincipal Jwt jwt,
                        Authentication authentication) {

                if (searchid != null) {
                        var realEstates = realEstateSerivce.getSearchRealEstates(searchid, page, size);
                        return ResponseEntity.ok(realEstates);
                }

                Page<RealEstateResponse> pagedRealEstates;

                boolean isEstateAgent = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority()
                                                .equals(Role.ESTATE_AGENT.name()));

                boolean isAdmin = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority()
                                                .equals(Role.ADMIN.name()));

                if (isEstateAgent) {
                        var estateAgentEmail = jwt.getSubject();
                        pagedRealEstates = realEstateSerivce.getEstateAgentRealEstates(estateAgentEmail, page, size);
                } else if (isAdmin) {
                        var adminEmail = jwt.getSubject();
                        pagedRealEstates = realEstateSerivce.getAdminRealEstates(adminEmail, page, size);
                } else {
                        pagedRealEstates = realEstateSerivce.getRealEstates(page, size);
                }

                return ResponseEntity.status(HttpStatus.OK).body(pagedRealEstates);
        }

        @PutMapping(value = "/{realestateid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<RealEstateResponse> updateRealEstate(
                        @PathVariable Long realestateid,
                        @RequestPart("data") @Valid RealEstateRequest request,
                        @RequestPart(value = "images", required = false) List<MultipartFile> images,
                        @AuthenticationPrincipal Jwt jwt,
                        Authentication authentication
        ) throws IOException {

                boolean isEstateAgent = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority().equals(Role.ESTATE_AGENT.name()));

                boolean isAdmin = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name()));

                if (!isEstateAgent && !isAdmin) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                String estateAgentEmail;

                if (isEstateAgent) {
                        estateAgentEmail = jwt.getSubject();
                } else {
                        var existing = realEstateSerivce.getRealEstateById(realestateid);
                        estateAgentEmail = existing.getEstateAgentEmail();
                }

                var realEstate = realEstateSerivce.updateRealEstate(realestateid, request, images, estateAgentEmail);
                return ResponseEntity.status(HttpStatus.OK).body(realEstate);
        }

        @DeleteMapping("/{realestateid}")
        public ResponseEntity<Void> deleteRealEstate(
                        @PathVariable Long realestateid,
                        @AuthenticationPrincipal Jwt jwt,
                        Authentication authentication
        ) {
                var stafferEmail = jwt.getSubject();
                var stafferRole = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(authority -> authority.equals(Role.ADMIN.name()) || authority.equals(Role.ESTATE_AGENT.name()))
                        .findFirst()
                        .orElseThrow(() -> new AccessDeniedException("Missing required role"));

                realEstateSerivce.deleteRealEstate(realestateid, stafferEmail, stafferRole);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
}
