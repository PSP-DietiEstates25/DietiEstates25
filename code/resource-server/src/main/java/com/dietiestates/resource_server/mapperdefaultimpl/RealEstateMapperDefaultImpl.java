package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.spec.RealEstateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RealEstateMapperDefaultImpl implements RealEstateMapper {

	@Override
	public RealEstateSpec toSpec(RealEstateRequest request, List<String> images) {
		return RealEstateSpec.builder()
				.category(request.getCategory())
				.images(images)
				.description(request.getDescription())
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
    public Page<RealEstateResponse> createPagedRealEstatesResponse(Page<RealEstate> realEstates) {
        return  realEstates.map(this::fromEntity);
    }

    @Override
    public List<RealEstateResponse> createRealEstatesResponse(List<RealEstate> realEstates) {
        var response = new ArrayList<RealEstateResponse>();

        realEstates.forEach(realEstate -> {
            response.add(fromEntity(realEstate));
        });

        return response;
    }
}
