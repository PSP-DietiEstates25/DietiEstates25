package com.dietiestates25.backend.controller;

import com.dietiestates25.backend.model.Ad;
import com.dietiestates25.backend.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
@CrossOrigin
public class AdController {

    @Autowired
    private AdRepository adRepository;

    @GetMapping
    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ad getAdById(@PathVariable Long id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @PostMapping
    public Ad createAd(@RequestBody Ad ad) {
        return adRepository.save(ad);
    }

    @PutMapping("/{id}")
    public Ad updateAd(@PathVariable Long id, @RequestBody Ad adDetails) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        // aggiorna i campi necessari
        ad.setDescription(adDetails.getDescription());
        // altri campi
        return adRepository.save(ad);
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Long id) {
        adRepository.deleteById(id);
    }

    @GetMapping("/agents/{agentId}/ads")
    public List<Ad> getAdsByAgent(@PathVariable Long agentId) {
        return adRepository.findByAgentId(agentId);
    }
}
