package com.dietiestates25.backend.controller;

import com.dietiestates25.backend.model.RealEstate;
import com.dietiestates25.backend.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estates")
@CrossOrigin
public class RealEstateController {

    @Autowired
    private RealEstateRepository realEstateRepository;

    @GetMapping
    public List<RealEstate> getAllEstates() {
        return realEstateRepository.findAll();
    }

    @GetMapping("/{id}")
    public RealEstate getEstateById(@PathVariable Long id) {
        return realEstateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("RealEstate not found"));
    }

    @PostMapping
    public RealEstate createEstate(@RequestBody RealEstate estate) {
        return realEstateRepository.save(estate);
    }

    /*
     
    @PutMapping("/{id}")
    public RealEstate updateEstate(@PathVariable Long id, @RequestBody RealEstate estateDetails) {
        RealEstate estate = realEstateRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("RealEstate not found"));
        estate.setName(estateDetails.getName());
        // ... altri campi ...
        return realEstateRepository.save(estate);
    }
    */

    @DeleteMapping("/{id}")
    public void deleteEstate(@PathVariable Long id) {
        realEstateRepository.deleteById(id);
    }
}
