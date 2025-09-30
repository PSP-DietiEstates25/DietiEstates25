package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.service.VisitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realEstates/{realestateid}/visits")
@RequiredArgsConstructor
public class VisitController {

	private final VisitService visitService;
	
	@PostMapping
	public ResponseEntity<Visit> createVisit(
			@RequestBody VisitRequest request,
			@PathVariable Long realestateid
			){
		visitService.createVisit(request, realestateid);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build(); 
	}
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
