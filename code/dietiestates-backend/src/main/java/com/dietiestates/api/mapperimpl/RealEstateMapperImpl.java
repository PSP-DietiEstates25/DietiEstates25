package com.dietiestates.api.mapperimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.spec.RealEstateSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateMapperImpl implements RealEstateMapper {
	
	@Override
	public RealEstateSpec toSpec(RealEstateRequest request) {
		return RealEstateSpec.builder()
				.category(request.getCategory())
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgentEmail(request.getEstateAgentEmail())
				.build();
	}
	
	@Override
	public RealEstateResponse fromEntity(RealEstate realEstate) {
		return RealEstateResponse.builder()
				.id(realEstate.getId())
				.createdDate(realEstate.getCreatedDate())
				.lastModifiedDate(realEstate.getLastModifiedDate())
				.category(realEstate.getCategory().toString())
				.images(realEstate.getImages())
				.description(realEstate.getDescription())
				.estateAgentEmail(realEstate.getEstateAgent().getSecurityAccountDecorator().getAccountEmail())
				.detailId(realEstate.getDetail().getId())
				.cadastralDataId(realEstate.getCadastralData().getId())
				.build();
	}

	@Override
	public List<RealEstateResponse> createRealEsatatesResponse(List<RealEstate> realEstates) {
		
		var response = new ArrayList<RealEstateResponse>();
		realEstates.forEach(realEstate -> {
			var realEstateResponse = fromEntity(realEstate);
			response.add(realEstateResponse);
		});
		
		return response;
	}
}
