package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;
import com.dietiestates.api.mapper.CadastralFilterMapper;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.spec.CadastralFilterSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterMapperImpl implements CadastralFilterMapper {
	
	@Override
	public CadastralFilterSpec toSpec(CadastralFilterRequest request) {
		return CadastralFilterSpec.builder()
				.minPrice(request.getMinPrice())
				.maxPrice(request.getMaxPrice())
				.minSquareMeters(request.getMinSquareMeters())
				.maxSquareMeters(request.getMaxSquareMeters())
				.minEnergyClass(request.getMinEnergyClass())
				.maxEnergyClass(request.getMaxEnergyClass())
				.minRooms(request.getMinRooms())
				.maxRooms(request.getMaxRooms())
				.minFloor(request.getMinFloor())
				.maxFloor(request.getMaxFloor())
				.build();
	}
	
	@Override
	public CadastralFilterResponse fromEntity(CadastralFilter cadastralFilter) {
		return CadastralFilterResponse.builder()
				.id(cadastralFilter.getId())
				.createdDate(cadastralFilter.getCreatedDate())
				.lastModifiedDate(cadastralFilter.getLastModifiedDate())
				.minPrice(cadastralFilter.getPriceRange().getMinPrice())
				.maxPrice(cadastralFilter.getPriceRange().getMaxPrice())
				.minSquareMeters(cadastralFilter.getSquareMetersRange().getMinSquareMeters())
				.maxSquareMeters(cadastralFilter.getSquareMetersRange().getMaxSquareMeters())
				.minEnergyClass(cadastralFilter.getEnergyClassRange().getMinEnergyClass())
				.maxEnergyClass(cadastralFilter.getEnergyClassRange().getMaxEnergyClass())
				.minRooms(cadastralFilter.getRoomsRange().getMinRooms())
				.maxRooms(cadastralFilter.getRoomsRange().getMaxRooms())
				.minFloor(cadastralFilter.getFloorRange().getMinFloor())
				.maxFloor(cadastralFilter.getFloorRange().getMaxFloor())
				.searchId(cadastralFilter.getSearch().getId())
				.build();
	}
}
