package com.dietiestates.resource_server.servicedefaultimpl.vecchi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealEstateAdQueryService {

	/*
    private final RealEstateAdRepository repository;

    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> myAds(String email, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return repository
                .findByEstateAgent_Email(email, pageable)
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
                        .postedByEmail(e.getEstateAgent().getEmail())
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

        return repository
                .search(category, emptyToNull(q), minPrice, maxPrice, minRooms, energy, pageable)
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
                        .postedByEmail(e.getEstateAgent().getEmail())
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
    */
}
