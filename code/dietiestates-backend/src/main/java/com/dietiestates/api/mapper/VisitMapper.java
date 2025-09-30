package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.dto.response.VisitResponse;
import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.model.Visit;

@Component
public class VisitMapper {

	public Visit toEntity(VisitRequest request, User user, RealEstate realEstate) {
		return Visit.visitBuilder()
				.createdDate(LocalDateTime.now())
				.category(ProposalCategory.valueOf(request.getCategory()))
				.status(ProposalStatus.valueOf(request.getStatus()))
				.user(user)
				.realEstate(realEstate)
				.date(request.getDate())
				.time(request.getTime())
				.build();
	}
	
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
