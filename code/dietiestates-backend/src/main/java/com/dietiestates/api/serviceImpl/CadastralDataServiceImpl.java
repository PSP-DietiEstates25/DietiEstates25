package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.factory.CadastralDataFactory;
import com.dietiestates.api.finder.CadastralDataFinder;
import com.dietiestates.api.mapper.CadastralDataMapper;
import com.dietiestates.api.repository.CadastralDataRepository;
import com.dietiestates.api.service.CadastralDataService;
import com.dietiestates.api.verifier.CadastralDataVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataServiceImpl implements CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	private final CadastralDataFactory cadastralDataFactory;
	private final CadastralDataFinder cadastralDataFinder;
	// private final CadastralDataVerifier cadastralDataVerifier;
	private final CadastralDataMapper cadastralDataMapper;

	@Override
	public CadastralDataResponse createCadastralData(CadastralDataRequest request) {

		var cadastralDataSpec = cadastralDataMapper.toSpec(request);

		var cadastralData = cadastralDataFactory.createCadastralDataFromSpec(cadastralDataSpec);
		cadastralDataRepository.save(cadastralData);

		return cadastralDataMapper.fromEntity(cadastralData);
	}

	@Override
	public CadastralDataResponse getCadastralDataById(Long cadastralDataId) {

		var cadastralData = cadastralDataFinder.getCadastralDataById(cadastralDataId);

		return cadastralDataMapper.fromEntity(cadastralData);
	}

	@Override
	@Transactional
	public CadastralDataResponse updateCadastralData(Long id, CadastralDataRequest request) {
		var entity = cadastralDataFinder.getCadastralDataById(id);

		if (request.getPrice() != null)
			entity.setPrice(request.getPrice());
		if (request.getRooms() != null)
			entity.setRooms(request.getRooms());
		if (request.getFloor() != null)
			entity.setFloor(request.getFloor());
		if (request.getEnergyClass() != null) {
			entity.setEnergyClass(EnergyClass.valueOf(request.getEnergyClass().toUpperCase()));
		}
		if (request.getSquareMeters() != null)
			entity.setSquareMeters(request.getSquareMeters());

		var saved = cadastralDataRepository.save(entity);
		return cadastralDataMapper.fromEntity(saved);
	}
}
