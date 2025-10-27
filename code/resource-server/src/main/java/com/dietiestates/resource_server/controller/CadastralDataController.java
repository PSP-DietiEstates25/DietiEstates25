package com.dietiestates.resource_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.resource_server.dto.request.CadastralDataRequest;
import com.dietiestates.resource_server.dto.response.CadastralDataResponse;
import com.dietiestates.resource_server.service.CadastralDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cadastraldata")
@RequiredArgsConstructor
public class CadastralDataController {

    private final CadastralDataService cadastralDataService;

    @PostMapping
    public ResponseEntity<CadastralDataResponse> createCadastralData(
            @RequestBody CadastralDataRequest request
    ){
        var cadastralData = cadastralDataService.createCadastralData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastralData);
    }

    @GetMapping("/{cadastraldataid}")
    public ResponseEntity<CadastralDataResponse> getCadastralDataById(
            @PathVariable Long cadastraldataid
    ){
        var cadastralData = cadastralDataService.getCadastralDataById(cadastraldataid);
        return ResponseEntity.status(HttpStatus.OK).body(cadastralData);
    }
}
