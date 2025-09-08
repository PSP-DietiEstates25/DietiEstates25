package com.dietiestates.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.api.dto.CreateDetailRequest;
import com.dietiestates.api.dto.IdResponse;
import com.dietiestates.api.service.DetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
@Validated
public class DetailController {

	/*
    private final DetailService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('AGENT','ADMIN', 'CLIENT')")
    public IdResponse create(@RequestBody @Valid CreateDetailRequest request) {
        Long id = service.create(request);
        return new IdResponse(id);
    }
    */
}
