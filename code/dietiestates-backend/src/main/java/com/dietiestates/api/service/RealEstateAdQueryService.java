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
import com.dietiestates.api.model.RealEstateAd;
import com.dietiestates.api.repository.RealEstateAdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateAdQueryService {

    private final RealEstateAdRepository adRepository;

    /** Nuovo: lista annunci dell'agente con paginazione (consigliato) */
    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> listMine(String agentEmail, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return toResponses(adRepository
                .findByEstateAgent_User_Email(agentEmail, pageable)
                .getContent());
    }

    /** Legacy (retro-compatibilità): prima pagina con size di default */
    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> listMine(String agentEmail) {
        return listMine(agentEmail, 0, 12);
    }

    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> search(
            AdCategory category,
            String q,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minRooms,
            EnergyClass energy,
            Integer page, // 0-based (page = 0 -> prima pagina (ad esempio annunci da 0 a 11))
            Integer size // es. 12
    ) {
        Pageable pageable = PageRequest.of(safePage(page), safeSize(size));
        return toResponses(
                adRepository.search(
                        category,
                        emptyToNull(q),
                        minPrice, maxPrice, minRooms, energy,
                        pageable).getContent());
    }

    private List<RealEstateAdResponse> toResponses(List<RealEstateAd> entities) {
        return entities.stream()
                .map(saved -> new RealEstateAdResponse(saved.getId(), saved.getCategory().name(),
                        saved.getDescription(), saved.getPrice(), saved.getSize(), saved.getAddress(), saved.getRooms(),
                        saved.getFloor(),
                        saved.getEnergyClass().name(), saved.getLatitude(), saved.getLongitude(),
                        saved.getEstateAgent().getUser().getEmail(), saved.getDetail().getId()))
                .toList();
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