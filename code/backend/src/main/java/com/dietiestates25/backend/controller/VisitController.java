package com.dietiestates25.backend.controller;

import com.dietiestates25.backend.model.Visit;
import com.dietiestates25.backend.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin
public class VisitController {

    @Autowired
    private VisitRepository visitRepository;

    @GetMapping
    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    @GetMapping("/{id}")
    public Visit getVisitById(@PathVariable Long id) {
        return visitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    @PostMapping
    public Visit createVisit(@RequestBody Visit visit) {
        return visitRepository.save(visit);
    }

    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable Long id) {
        visitRepository.deleteById(id);
    }
}
