package com.dietiestates.resource_server.servicedefaultimpl.vecchi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitQueryService {

	/*
    private final VisitRepository visitRepo;
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Rome");

    @Transactional(readOnly = true)
    public List<VisitResponse> myInbox(String agentEmail, VisitStatus status, Integer page, Integer size) {
        Pageable p = PageRequest.of(safePage(page), safeSize(size));

        var pageObj = (status == null)
                ? visitRepo.findByEstateAgent_EmailOrderByStartAtDesc(agentEmail, p)
                : visitRepo.findByEstateAgent_EmailAndStatusOrderByStartAtDesc(agentEmail, status, p);

        return pageObj
                .map(v -> VisitResponse.builder()
                        .id(v.getId())
                        .adId(v.getRealEstate() != null ? v.getRealEstate().getId() : null)
                        .adAddress(v.getRealEstate() != null ? v.getRealEstate().getAddress() : null)
                        .requesterEmail(v.getUser() != null ? v.getUser().getEmail() : null)
                        .agentEmail(v.getEstateAgent() != null ? v.getEstateAgent().getEmail() : null)
                        .status(v.getStatus() != null ? v.getStatus().name() : null)
                        .startAt(v.getStartAt())
                        .createdAt(v.getCreatedDate() != null
                                ? v.getCreatedDate().atZone(DEFAULT_ZONE).toInstant()
                                : null)
                        .build())
                .getContent();
    }

    private int safePage(Integer p) {
        return (p != null && p >= 0) ? p : 0;
    }

    private int safeSize(Integer s) {
        return (s != null && s > 0) ? s : 12;
    }
    */
}
