package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.mapper.CadastralDataMapper;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.spec.CadastralDataSpec;

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
				.energyClass(cadastralData.getEnergyClass().toString())
				.rooms(cadastralData.getRooms())
				.floor(cadastralData.getFloor())
				.realEstateId(cadastralData.getRealEstate().getId())
				.build();
	}
}
