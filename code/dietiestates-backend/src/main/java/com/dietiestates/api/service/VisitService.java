package com.dietiestates.api.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.enums.NotificationCategoryType;
//import com.dietiestates.api.enums.VisitStatus;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.repository.RealEstateAdRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitService {

	/*
    private final VisitRepository visitRepo;
    private final RealEstateAdRepository adRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Rome");
    

    @Transactional
    public VisitResponse propose(String requesterEmail, CreateVisitRequest req) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requesterEmail));

        RealEstate ad = adRepo.findById(req.adId())
                .orElseThrow(() -> new IllegalArgumentException("Ad not found: " + req.adId()));

        User agent = ad.getEstateAgent();

        ZoneId zone = DEFAULT_ZONE;
        if (req.timezone() != null && !req.timezone().isBlank()) {
            try {
                zone = ZoneId.of(req.timezone());
            } catch (Exception ignored) {
            }
        }
        LocalDate date = LocalDate.parse(req.date());
        LocalTime time = LocalTime.of(req.hour(), req.minute());
        Instant start = ZonedDateTime.of(date, time, zone).toInstant();

        boolean busy = visitRepo.existsAgentSlot(
                agent.getEmail(),
                start,
                List.of(VisitStatus.PENDING, VisitStatus.CONFIRMED));
        if (busy) {
            throw new IllegalStateException("Selected time slot is already taken for the agent.");
        }

        Visit entity = Visit.builder()
                .realEstate(ad)
                .user(requester)
                .estateAgent(agent)
                .startAt(start)
                .status(VisitStatus.PENDING)
                .build();

        var saved = visitRepo.save(entity);

        notificationService.push(
                agent.getEmail(),
                NotificationCategoryType.VISIT,
                "Nuova richiesta visita",
                "Hai una nuova richiesta di visita per l'annuncio: " + ad.getAddress(),
                ad.getId());

        return toResponse(saved);
    }

    @Transactional
    public VisitResponse confirm(String agentEmail, Long visitId) {
        Visit v = visitRepo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + visitId));
        if (!v.getEstateAgent().getEmail().equals(agentEmail)) {
            throw new AccessDeniedException("Not your visit request.");
        }
        if (v.getStatus() != VisitStatus.PENDING) {
            throw new IllegalStateException("Only PENDING visits can be confirmed.");
        }
        v.setStatus(VisitStatus.CONFIRMED);
        var saved = visitRepo.save(v);

        notificationService.push(
                v.getUser().getEmail(),
                NotificationCategoryType.VISIT,
                "Visita confermata",
                "La tua richiesta di visita per \"" + v.getRealEstate().getAddress() + "\" è stata confermata.",
                v.getRealEstate().getId());

        return toResponse(saved);
    }

    @Transactional
    public VisitResponse decline(String agentEmail, Long visitId) {
        Visit v = visitRepo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + visitId));
        if (!v.getEstateAgent().getEmail().equals(agentEmail)) {
            throw new AccessDeniedException("Not your visit request.");
        }
        if (v.getStatus() != VisitStatus.PENDING) {
            throw new IllegalStateException("Only PENDING visits can be declined.");
        }

        v.setStatus(VisitStatus.DECLINED);
        var saved = visitRepo.save(v);

        notificationService.push(
                v.getUser().getEmail(),
                NotificationCategoryType.VISIT,
                "Visita rifiutata",
                "La tua richiesta di visita per \"" + v.getRealEstate().getAddress() + "\" è stata rifiutata.",
                v.getRealEstate().getId());

        return toResponse(saved);
    }

    private VisitResponse toResponse(Visit v) {

        Instant createdAtInstant = null;
        if (v.getCreatedDate() != null) {
            createdAtInstant = v.getCreatedDate()
                    .atZone(DEFAULT_ZONE) // interpreta il LocalDateTime in Europe/Rome
                    .toInstant();
        }

        return VisitResponse.builder()
                .id(v.getId())
                .adId(v.getRealEstate().getId())
                .adAddress(v.getRealEstate().getAddress())
                .requesterEmail(v.getUser().getEmail())
                .agentEmail(v.getEstateAgent().getEmail())
                .status(v.getStatus().name())
                .startAt(v.getStartAt())
                .createdAt(createdAtInstant)
                .build();
    }
    
    */
}
