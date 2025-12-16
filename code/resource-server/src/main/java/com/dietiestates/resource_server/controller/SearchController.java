package com.dietiestates.resource_server.controller;

import java.util.List;

import com.dietiestates.resource_server.dto.response.SearchResponse;
import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.service.SearchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<List<RealEstateResponse>> createSearch(
            @RequestBody @Valid SearchRequest request,
            @AuthenticationPrincipal Jwt jwt
    ){
        var userEmail = jwt.getSubject();

        var realEstates = searchService.createSearch(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(realEstates);
    }

    @GetMapping("/{searchid}")
    public ResponseEntity<List<RealEstateResponse>> getSearch(
            @PathVariable Long searchid,
            @AuthenticationPrincipal Jwt jwt
    ){
        var userEmail = jwt.getSubject();

        var realEstates = searchService.runSavedSearch(searchid, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(realEstates);
    }

    @GetMapping
    public ResponseEntity<Page<SearchResponse>> getUserSearches(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @AuthenticationPrincipal Jwt jwt
    ){
        var userEmail = jwt.getSubject();

        var searches = searchService.getUserSearches(userEmail, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(searches);
    }
}

