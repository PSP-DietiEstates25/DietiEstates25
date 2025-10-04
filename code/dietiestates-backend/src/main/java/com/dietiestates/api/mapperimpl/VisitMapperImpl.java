package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.mapper.VisitMapper;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.spec.VisitSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitMapperImpl implements VisitMapper {
	
	@Override
	public VisitSpec toSpec(VisitRequest request) {
		return VisitSpec.visitSpecBuilder()
				.category(request.getCategory())
				.status(request.getStatus())
				.userEmail(request.getUserEmail())
				.date(request.getDate())
				.time(request.getTime())
				.build();
	}
	
	@Override
	public VisitResponse fromEntity(Visit visit) {
		return VisitResponse.visitResponseBuilder()
				.id(visit.getId())
				.createdDate(visit.getCreatedDate())
				.lastModifiedDate(visit.getLastModifiedDate())
				.category(visit.getProposalCategory().toString())
				.status(visit.getProposalStatus().toString())
				.userEmail(visit.getUser().getEmail())
				.realEstateId(visit.getRealEstate().getId())
				.date(visit.getDate())
				.time(visit.getTime())
				.build();
	}
}
