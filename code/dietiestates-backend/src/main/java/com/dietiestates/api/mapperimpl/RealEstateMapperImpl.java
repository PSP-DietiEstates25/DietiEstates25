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
    .images(request.getImages() != null ? request.getImages().toArray(String[]::new) : null)
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
								realEstate.getEstateAgent().getSecurityAccountDecorator() != null
										? realEstate.getEstateAgent().getSecurityAccountDecorator().getAccountEmail()
										: null)
				.detailId(realEstate.getDetail() != null ? realEstate.getDetail().getId() : null)
				.cadastralDataId(realEstate.getCadastralData() != null ? realEstate.getCadastralData().getId() : null)
				.proximityTags(realEstate.getProximityTags() != null
						? realEstate.getProximityTags().stream().map(Enum::name).toArray(String[]::new)
						: null)
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
