package com.dietiestates.resource_server.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.resource_server.dto.request.CadastralFilterRequest;
import com.dietiestates.resource_server.dto.response.CadastralFilterResponse;
import com.dietiestates.resource_server.service.CadastralFilterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cadastralfilters")
@RequiredArgsConstructor
public class    CadastralFilterController {

    private final CadastralFilterService cadastralFilterService;

    @PostMapping
    public ResponseEntity<CadastralFilterResponse> createCadastralFilter(
            @RequestBody CadastralFilterRequest request
    ){
        var cadastralFilter = cadastralFilterService.createCadastralFilter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastralFilter);
    }

    @GetMapping("/{cadastralfilterid}")
    public ResponseEntity<CadastralFilterResponse> getCadastralFilterById(
            @PathVariable Long cadastralfilterid
    ){
        var cadastralFilter = cadastralFilterService.getCadastralFilterById(cadastralfilterid);
        return ResponseEntity.status(HttpStatus.OK).body(cadastralFilter);
    }

    @GetMapping
    public ResponseEntity<CadastralFilterResponse> getRealEstateCadastralFilter(
            @RequestParam Long realestateid
    ){
        return null;
    }
}
