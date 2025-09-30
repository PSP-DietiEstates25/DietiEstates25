package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitService {
	
	private final VisitRepository visitRepository;
	private final RealEstateRepository realEstateRepository;
	private final UserRepository userRepository;
	
	public void createVisit(VisitRequest request, Long realEstateId) {
		var visit = of(request, realEstateId);
		visitRepository.save(visit);
	}
	
	public Visit of(VisitRequest request, Long realEstateId) {
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		var realEstate = realEstateRepository.findById(realEstateId)
				.orElseThrow(RealEstateNotFoundException::new);
		
		return Visit.visitBuilder()
				.createdDate(LocalDateTime.now())
				.category(ProposalCategory.VISIT)
				.status(ProposalStatus.PENDING)
				.user(user)
				.realEstate(realEstate)
				.date(request.getDate())
				.time(request.getTime())
				.build();
	}

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
