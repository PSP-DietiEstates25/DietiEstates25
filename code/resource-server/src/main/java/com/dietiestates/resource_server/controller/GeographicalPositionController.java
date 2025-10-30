package com.dietiestates.resource_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;
import com.dietiestates.resource_server.service.GeographicalPositionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/geographicalpositions")
@RequiredArgsConstructor
public class GeographicalPositionController {

    private final GeographicalPositionService geographicalPositionService;

    @PostMapping
    public ResponseEntity<GeographicalPositionResponse> createGeographicalPosition(
            @RequestBody GeographicalPositionRequest request
    ){
        var geographicalPosition = geographicalPositionService.createGeographicalPosition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(geographicalPosition);
    }

    @GetMapping("/{geographicalpositionid}")
    public ResponseEntity<GeographicalPositionResponse> getGeographicalPositionById(
            @PathVariable Long geographicalpositionid
    ){
        var geographicalPosition = geographicalPositionService.getGeographicalPositionById(geographicalpositionid);
        return ResponseEntity.status(HttpStatus.OK).body(geographicalPosition);
    }

    @PutMapping("/{geographicalpositionid}")
    public ResponseEntity<GeographicalPositionResponse> updateGeographicalPosition(
            @PathVariable Long geographicalpositionid,
            @RequestBody GeographicalPositionRequest request
    ) {

        geographicalPositionService.updateGeographicalPosition(geographicalpositionid, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
