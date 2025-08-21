package com.dietiestates.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.repository.RealEstateAdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateAdQueryService {

    private final RealEstateAdRepository adRepository;

    /** Lista annunci dell’utente corrente (AGENT/ADMIN) con paginazione */
    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> myAds(String email, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return adRepository.findByPostedBy_Email(email, pageable)
                .map(e -> RealEstateAdResponse.builder()
                        .id(e.getId())
                        .category(e.getCategory().name())
                        .description(e.getDescription())
                        .price(e.getPrice())
                        .size(e.getSize())
                        .address(e.getAddress())
                        .rooms(e.getRooms())
                        .floor(e.getFloor())
                        .energyClass(e.getEnergyClass().name())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .postedByEmail(e.getPostedBy().getEmail())
                        .detailId(e.getDetail().getId())
                        .build())
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> search(
            AdCategory category,
            String q,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minRooms,
            EnergyClass energy,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return adRepository.search(
                category,
                emptyToNull(q),
                minPrice,
                maxPrice,
                minRooms,
                energy,
                pageable)
                .map(e -> RealEstateAdResponse.builder()
                        .id(e.getId())
                        .category(e.getCategory().name())
                        .description(e.getDescription())
                        .price(e.getPrice())
                        .size(e.getSize())
                        .address(e.getAddress())
                        .rooms(e.getRooms())
                        .floor(e.getFloor())
                        .energyClass(e.getEnergyClass().name())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .postedByEmail(e.getPostedBy().getEmail())
                        .detailId(e.getDetail().getId())
                        .build())
                .getContent();
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private int safePage(Integer p) {
        return (p != null && p >= 0) ? p : 0;
    }

    private int safeSize(Integer s) {
        return (s != null && s > 0) ? s : 12;
    }
}
