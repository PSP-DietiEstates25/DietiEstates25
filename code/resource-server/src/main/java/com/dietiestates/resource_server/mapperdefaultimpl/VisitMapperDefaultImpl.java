package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.VisitRequest;
import com.dietiestates.resource_server.dto.response.VisitResponse;
import com.dietiestates.resource_server.mapper.VisitMapper;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VisitMapperDefaultImpl implements VisitMapper {
	
	@Override
	public VisitSpec toSpec(VisitRequest request) {
		return VisitSpec.visitSpecBuilder()
				.category(request.getCategory())
				.status(request.getStatus())
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
				.userEmail(visit.getNegotiation().getUser().getEmail())
				.realEstateId(visit.getNegotiation().getRealEstate().getId())
                .estateAgentEmail(visit.getNegotiation().getEstateAgent().getEmail())
                .realEstateAddress(visit
                        .getNegotiation()
                        .getRealEstate()
                        .getDetail()
                        .getGeographicalPosition()
                        .getAddress()
                )
				.date(visit.getDate())
				.time(visit.getTime())
				.build();
	}

    @Override
    public List<VisitResponse> createVisitsResponse(List<Visit> visits) {
        var visitsResponse = new ArrayList<VisitResponse>();
        visits.forEach(visit ->
            visitsResponse.add(this.fromEntity(visit))
        );

        return visitsResponse;
    }

    @Override
    public Page<VisitResponse> createPagedVisitsResponse(Page<Visit> visits) {
        return visits.map(this::fromEntity);
    }
}
