package com.dietiestates25.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*") // per permettere richieste da Angular
public class LocationController {

    private final List<String> allLocations = List.of("Naples", "Rome", "Milan", "Florence", "Turin");

    @GetMapping("/search")
    public List<String> search(@RequestParam String query) {
        return allLocations.stream()
                .filter(loc -> loc.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
// abbiamo un endpoint che restituisce una lista di località in base a una query di ricerca.
// La lista di località è hardcoded per semplicità, ma potrebbe provenire da un database.