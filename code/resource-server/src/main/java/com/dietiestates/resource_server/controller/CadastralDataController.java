package com.dietiestates.resource_server.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<CadastralDataResponse> getRealEstateCadastralData(
            @RequestParam Long realestateid
    ){
        return null;
    }

    @GetMapping("/{cadastraldataid}")
    public ResponseEntity<CadastralDataResponse> getCadastralDataById(
            @PathVariable Long cadastraldataid
    ){
        var cadastralData = cadastralDataService.getCadastralDataById(cadastraldataid);
        return ResponseEntity.status(HttpStatus.OK).body(cadastralData);
    }

    @PutMapping("/{cadastraldataid}")
    public ResponseEntity<CadastralDataResponse> updateCadastralData(
            @PathVariable Long cadastraldataid,
            @RequestBody CadastralDataRequest request
    ) {
        cadastralDataService.updateCadastralData(cadastraldataid, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
