package com.dietiestates25.backend.controller;

import com.dietiestates25.backend.model.Offer;
import com.dietiestates25.backend.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    @GetMapping
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Offer getOfferById(@PathVariable Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    @PostMapping
    public Offer createOffer(@RequestBody Offer offer) {
        return offerRepository.save(offer);
    }

    @GetMapping("/ads/{adId}/offers")
    public List<Offer> getOffersForAd(@PathVariable Long adId) {
        return offerRepository.findByAdId(adId);
    }
}