package com.dietiestates.api.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.VisitResponse;
import com.dietiestates.api.enums.VisitStatus;
import com.dietiestates.api.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitQueryService {

    private final VisitRepository visitRepo;

    @Transactional(readOnly = true)
    public List<VisitResponse> myInbox(String agentEmail, VisitStatus status, Integer page, Integer size) {
        Pageable p = PageRequest.of(s(page), s(size));
        var pageObj = (status == null)
                ? visitRepo.findByAgent_EmailOrderByStartAtDesc(agentEmail, p)
                : visitRepo.findByAgent_EmailAndStatusOrderByStartAtDesc(agentEmail, status, p);

        return pageObj
                .map(v -> VisitResponse.builder()
                        .id(v.getId())
                        .adId(v.getAd().getId())
                        .adAddress(v.getAd().getAddress())
                        .requesterEmail(v.getRequester().getEmail())
                        .agentEmail(v.getAgent().getEmail())
                        .status(v.getStatus().name())
                        .startAt(v.getStartAt())
                        .createdAt(v.getCreatedAt())
                        .build())
                .getContent();
    }

    private int s(Integer x) {
        return (x != null && x > 0) ? x : 12;
    }
}
