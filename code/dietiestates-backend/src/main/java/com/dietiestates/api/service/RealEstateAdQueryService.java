package com.dietiestates.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<RealEstateAdResponse> listMine(String agentEmail) {
        return toResponses(adRepository.findByEstateAgent_Email(agentEmail));
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
        List<RealEstateAd> all = adRepository.search(category,
                emptyToNull(q),
                minPrice, maxPrice, minRooms, energy);

        // sort per esempio per prezzo crescente
        // all.sort(Comparator.comparing(RealEstateAd::getPrice));

        // paging manuale
        /**
         * page=0, size=12 -> mostra annunci [0..11]
         * page=1, size=12 -> mostra annunci [12..23]
         */
        int p = page != null && page >= 0 ? page : 0;
        int s = size != null && size > 0 ? size : 12;
        int from = Math.min(p * s, all.size());
        int to = Math.min(from + s, all.size());

        return toResponses(all.subList(from, to));
    }

    private List<RealEstateAdResponse> toResponses(List<RealEstateAd> entities) {
        List<RealEstateAdResponse> res = new ArrayList<>();
        for (RealEstateAd saved : entities) {
            res.add(new RealEstateAdResponse(
                    saved.getId(),
                    saved.getCategory().name(),
                    saved.getDescription(),
                    saved.getPrice(),
                    saved.getSize(),
                    saved.getAddress(),
                    saved.getRooms(),
                    saved.getFloor(),
                    saved.getEnergyClass().name(),
                    saved.getLatitude(),
                    saved.getLongitude(),
                    saved.getEstateAgent().getUser().getEmail(),
                    saved.getDetail().getId()));
        }
        return res;
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}