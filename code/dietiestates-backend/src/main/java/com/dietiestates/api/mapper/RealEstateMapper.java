package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.spec.RealEstateSpec;

@Component
public class RealEstateMapper {

	public RealEstate toEntity(RealEstateRequest request, EstateAgent estateAgent) {
		return RealEstate.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgent(estateAgent)
				.build();
	}
	
	public RealEstateSpec toSpec(RealEstateRequest request) {
		return RealEstateSpec.builder()
				.category(request.getCategory())
				.images(request.getImages())
				.description(request.getDescription())
				.estateAgentEmail(request.getEstateAgentEmail())
				.build();
	}
	
	public RealEstateResponse fromEntity(RealEstate realEstate) {
		return RealEstateResponse.builder()
				.id(realEstate.getId())
				.createdDate(realEstate.getCreatedDate())
				.lastModifiedDate(realEstate.getLastModifiedDate())
				.category(realEstate.getCategory().toString())
				.images(realEstate.getImages())
				.description(realEstate.getDescription())
				.estateAgentEmail(realEstate.getEstateAgent().getEmail())
				.detailId(realEstate.getDetail().getId())
				.cadastralDataId(realEstate.getCadastralData().getId())
				.build();
	}
}
