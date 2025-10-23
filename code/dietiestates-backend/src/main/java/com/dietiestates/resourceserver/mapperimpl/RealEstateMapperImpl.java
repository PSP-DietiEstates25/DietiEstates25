package com.dietiestates.resourceserver.mapperimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.RealEstateRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.mapper.RealEstateMapper;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.spec.RealEstateSpec;

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
				.cadastralDataId(request.getCadastralDataId())
				.detailId(request.getDetailId())
				.build();
	}

	@Override
	public RealEstateResponse fromEntity(RealEstate realEstate) {
		final String[] imagesArr = realEstate.getImages() != null
				? realEstate.getImages().toArray(String[]::new)
				: new String[0];

		return RealEstateResponse.builder()
				.id(realEstate.getId())
				.createdDate(realEstate.getCreatedDate())
				.lastModifiedDate(realEstate.getLastModifiedDate())
				.category(realEstate.getCategory() != null ? realEstate.getCategory().name() : null)
				.images(imagesArr)
				.description(realEstate.getDescription())
				.estateAgentEmail(
						realEstate.getEstateAgent() != null &&
								realEstate.getEstateAgent().getEmail() != null
										? realEstate.getEstateAgent().getEmail()
										: null)
				.detailId(realEstate.getDetail() != null ? realEstate.getDetail().getId() : null)
				.cadastralDataId(realEstate.getCadastralData() != null ? realEstate.getCadastralData().getId() : null)
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
