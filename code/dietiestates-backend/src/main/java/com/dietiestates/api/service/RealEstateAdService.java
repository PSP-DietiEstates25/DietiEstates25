package com.dietiestates.api.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dietiestates.api.dto.CreateRealEstateAdRequest;
import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.RealEstateAd;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RealEstateAdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateAdService {

        private final RealEstateAdRepository adRepository;

        private final EstateAgentRepository estateAgentRepository;

        private final DetailRepository detailRepository;

        @Transactional // la creazione dell’annuncio avviene in una transazione DB: se qualcosa
                       // fallisce a metà, tutte le operazioni vengono annullate (rollback)

        public RealEstateAdResponse create(CreateRealEstateAdRequest req,
                        MultipartFile photo,
                        String agentEmail) throws IOException {

                if (photo == null || photo.isEmpty()) {
                        throw new IllegalArgumentException("Photo is required");
                }
                if (photo.getSize() > 5 * 1024 * 1024) { // 5MB esempio
                        throw new IllegalArgumentException("Photo too large (max 5MB)");
                }
                String contentType = photo.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                        throw new IllegalArgumentException("Invalid photo type");
                }

                EstateAgent agent = estateAgentRepository.findByUser_Email(agentEmail)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "EstateAgent not found: " + agentEmail));

                Detail detail = detailRepository.findById(req.detailId())
                                .orElseThrow(() -> new IllegalArgumentException("Detail not found: " + req.detailId()));

                RealEstateAd ad = new RealEstateAd();
                ad.setCategory(req.category());
                ad.setPhoto(photo.getBytes());
                ad.setDescription(req.description());

                ad.setPrice(req.price());
                ad.setSize(req.size());
                ad.setAddress(req.address());
                ad.setRooms(req.rooms());
                ad.setFloor(req.floor());
                ad.setEnergyClass(req.energyClass());
                ad.setLatitude(req.latitude());
                ad.setLongitude(req.longitude());

                ad.attachEstateAgent(agent);
                ad.attachDetail(detail);

                RealEstateAd saved = adRepository.save(ad);

                // response
                return new RealEstateAdResponse(
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
                                saved.getDetail().getId());
        }
}
