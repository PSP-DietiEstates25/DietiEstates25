package com.dietiestates.api.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dietiestates.api.dto.CreateRealEstateAdRequest;
import com.dietiestates.api.dto.RealEstateAdResponse;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.DetailRepository;
import com.dietiestates.api.repository.RealEstateAdRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateAdService {

        private final RealEstateAdRepository adRepository;
        private final UserRepository userRepository;
        private final DetailRepository detailRepository;

        @Transactional
        public RealEstateAdResponse create(
                        CreateRealEstateAdRequest req,
                        MultipartFile photo,
                        String userEmail) throws IOException {

                if (photo == null || photo.isEmpty()) {
                        throw new IllegalArgumentException("Photo is required");
                }
                if (photo.getSize() > 5 * 1024 * 1024) { // 5MB
                        throw new IllegalArgumentException("Photo too large (max 5MB)");
                }

                // chi pubblica (AGENT o ADMIN in base ai ruoli associati all'utente)
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

                // verifica ruolo: AGENT o ADMIN
                boolean canPost = user.getRoles().stream()
                                .anyMatch(r -> "AGENT".equalsIgnoreCase(r.getName())
                                                || "ADMIN".equalsIgnoreCase(r.getName()));
                if (!canPost) {
                        throw new SecurityException("User not allowed to publish ads");
                }

                // Detail
                Detail detail = detailRepository.findById(req.detailId())
                                .orElseThrow(() -> new IllegalArgumentException("Detail not found: " + req.detailId()));

                // Entity
                RealEstate ad = new RealEstate();
                ad.setCategory(req.category());
                ad.setDescription(req.description());
                ad.setPrice(req.price());
                ad.setSize(req.size());
                ad.setAddress(req.address());
                ad.setRooms(req.rooms());
                ad.setFloor(req.floor());
                ad.setEnergyClass(req.energyClass());
                ad.setLatitude(req.latitude());
                ad.setLongitude(req.longitude());
                ad.setPhoto(photo.getBytes());
                ad.attachPostedBy(user);
                ad.attachDetail(detail);

                RealEstate saved = adRepository.save(ad);

                return RealEstateAdResponse.builder()
                                .id(saved.getId())
                                .category(saved.getCategory().name())
                                .description(saved.getDescription())
                                .price(saved.getPrice())
                                .size(saved.getSize())
                                .address(saved.getAddress())
                                .rooms(saved.getRooms())
                                .floor(saved.getFloor())
                                .energyClass(saved.getEnergyClass().name())
                                .latitude(saved.getLatitude())
                                .longitude(saved.getLongitude())
                                .postedByEmail(saved.getPostedBy().getEmail())
                                .detailId(saved.getDetail().getId())
                                .build();
        }
}
