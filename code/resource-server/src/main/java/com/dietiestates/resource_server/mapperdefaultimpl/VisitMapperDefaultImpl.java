package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.mapper.VisitMapper;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitMapperDefaultImpl implements VisitMapper {
	
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
