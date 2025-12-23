package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.CadastralDataRequest;
import com.dietiestates.resource_server.dto.response.CadastralDataResponse;
import com.dietiestates.resource_server.mapper.CadastralDataMapper;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.spec.CadastralDataSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralDataMapperDefaultImpl implements CadastralDataMapper {
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
				.build();
	}
}
