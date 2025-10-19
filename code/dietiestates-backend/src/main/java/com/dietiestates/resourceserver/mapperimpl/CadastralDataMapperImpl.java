package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.CadastralDataRequest;
import com.dietiestates.resourceserver.dto.response.CadastralDataResponse;
import com.dietiestates.resourceserver.mapper.CadastralDataMapper;
import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.spec.CadastralDataSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataMapperImpl implements CadastralDataMapper {
	@Override
	public CadastralDataSpec toSpec(CadastralDataRequest request) {
		return CadastralDataSpec.builder()
				.price(request.getPrice())
				.squareMeters(request.getSquareMeters())
				.energyClass(request.getEnergyClass())
				.rooms(request.getRooms())
				.floor(request.getFloor())
				.build();
	}

	@Override
	public CadastralDataResponse fromEntity(CadastralData cadastralData) {
		return CadastralDataResponse.builder()
				.id(cadastralData.getId())
				.createdDate(cadastralData.getCreatedDate())
				.lastModifiedDate(cadastralData.getLastModifiedDate())
				.price(cadastralData.getPrice())
				.squareMeters(cadastralData.getSquareMeters())
				.energyClass(cadastralData.getEnergyClass() != null ? cadastralData.getEnergyClass().toString() : null)
				.rooms(cadastralData.getRooms())
				.floor(cadastralData.getFloor())
				.realEstateId(cadastralData.getRealEstate() != null ? cadastralData.getRealEstate().getId() : null)
				.build();
	}
}
