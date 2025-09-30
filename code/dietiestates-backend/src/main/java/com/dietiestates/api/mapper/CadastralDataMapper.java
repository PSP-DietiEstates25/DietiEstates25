package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.model.RealEstate;

@Component
public class CadastralDataMapper {

	public CadastralData toEntity(CadastralDataRequest request, RealEstate realEstate) {
		return CadastralData.cadastralDataBuilder()
				.createdDate(LocalDateTime.now())
				.price(request.getPrice())
				.squareMeters(request.getSquareMeters())
				.energyClass(request.getEnergyClass())
				.rooms(request.getRooms())
				.floor(request.getFloor())
				.realEstate(realEstate)
				.build();
	}
	
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
