package com.dietiestates.resource_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.DetailRequest;
import com.dietiestates.resource_server.dto.response.DetailResponse;
import com.dietiestates.resource_server.service.DetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class DetailController {

    private final DetailService detailService;

    @PostMapping
    public ResponseEntity<DetailResponse> createDetail(
            @RequestBody DetailRequest request
    ) {
        var detail = detailService.createDetail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(detail);
    }

    @GetMapping("/{detailid}")
    public ResponseEntity<DetailResponse> getDetailById(
            @PathVariable Long detailid
    ){
        var detail = detailService.getDetailById(detailid);
        return ResponseEntity.status(HttpStatus.OK).body(detail);
    }

    @GetMapping
    public ResponseEntity<DetailResponse> getRealEstateDetail(
            @RequestParam Long realestateid
    ){
        return null;
    }

    @GetMapping
    public ResponseEntity<DetailResponse> getSearchDetail(
            @RequestParam Long searchid
    ){
        return null;
    }

    @PutMapping("/{detailid}")
    public ResponseEntity<DetailResponse> updateDetail(
            @PathVariable Long detailid,
            @RequestBody DetailRequest request
    ) {

        detailService.updateDetail(detailid, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

