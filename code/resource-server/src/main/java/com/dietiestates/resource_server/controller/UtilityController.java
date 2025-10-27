package com.dietiestates.resource_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resource_server.dto.request.UtilityRequest;
import com.dietiestates.resource_server.dto.response.UtilityResponse;
import com.dietiestates.resource_server.service.UtilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/utilities")
@RequiredArgsConstructor
public class UtilityController {

    private final UtilityService utilityService;

    @PostMapping
    public ResponseEntity<UtilityResponse> createUtility(
            @RequestBody UtilityRequest request
    ){
        var utility = utilityService.createUtility(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(utility);
    }

    @GetMapping("/{utilityid}")
    public ResponseEntity<UtilityResponse> getUtilityById(
            @PathVariable Long utilityid
    ){
        var utility = utilityService.getUtilityById(utilityid);
        return ResponseEntity.status(HttpStatus.OK).body(utility);
    }
}
