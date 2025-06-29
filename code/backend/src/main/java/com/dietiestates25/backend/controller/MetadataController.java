package com.dietiestates25.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import com.dietiestates25.backend.model.AdType;

@RestController
@RequestMapping("/api/metadata")
@CrossOrigin
public class MetadataController {

    // Endpoint: GET /api/metadata/categories
    @GetMapping("/categories")
    public List<String> getCategories() {
        // Restituisce i nomi delle categorie dalla enum
        return Arrays.stream(AdType.values())
                     .map(Enum::name)
                     .collect(Collectors.toList());
    }

    /*
     
    // Endpoint: GET /api/metadata/services
    @GetMapping("/services")
    public List<ServiceDto> getServices() {
        // Lista dei servizi disponibili come hardcoded, in ordine e con id numerico
        List<ServiceDto> services = new ArrayList<>();
        services.add(new ServiceDto(1L, "Elevator"));
        services.add(new ServiceDto(2L, "Doorman"));
        services.add(new ServiceDto(3L, "AirCondition"));
        return services;
    }
        */
    }