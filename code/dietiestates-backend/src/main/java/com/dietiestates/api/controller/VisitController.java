package com.dietiestates.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.service.VisitQueryService;
import com.dietiestates.api.service.VisitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

	/* 
    private final VisitService visitService;
    private final VisitQueryService visitQuery;

    
    
    // cliente: propone visita
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public VisitResponse propose(Authentication auth, @Valid @RequestBody CreateVisitRequest body) {
        return visitService.propose(auth.getName(), body);
    }

    // agente: inbox richieste per i miei annunci
    @GetMapping("/inbox")
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
    public List<VisitResponse> myInbox(
            Authentication auth,
            @RequestParam(required = false) VisitStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size) {
        return visitQuery.myInbox(auth.getName(), status, page, size);
    }

    // agente: conferma
    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
    public VisitResponse confirm(Authentication auth, @PathVariable Long id) {
        return visitService.confirm(auth.getName(), id);
    }

    // agente: rifiuta
    @PatchMapping("/{id}/decline")
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN')")
    public VisitResponse decline(Authentication auth, @PathVariable Long id) {
        return visitService.decline(auth.getName(), id);
    }
    */
}
