package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.CadastralDataRequest;
import com.dietiestates.resource_server.dto.response.CadastralDataResponse;
import com.dietiestates.resource_server.enums.EnergyClass;
import com.dietiestates.resource_server.factory.CadastralDataFactory;
import com.dietiestates.resource_server.finder.CadastralDataFinder;
import com.dietiestates.resource_server.mapper.CadastralDataMapper;
import com.dietiestates.resource_server.repository.CadastralDataRepository;
import com.dietiestates.resource_server.service.CadastralDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastralDataServiceDefaultImpl implements CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	private final CadastralDataFactory cadastralDataFactory;
	private final CadastralDataFinder cadastralDataFinder;
	//private final CadastralDataVerifier cadastralDataVerifier;
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
    public CadastralDataResponse getRealEstateCadastralData(Long realEstateId) {
        var cadastralData =  cadastralDataFinder.getRealEstateCadastralData(realEstateId);
        return cadastralDataMapper.fromEntity(cadastralData);
    }

    @Override
    public void updateCadastralData(Long cadastralDataId, CadastralDataRequest request) {

        var cadastralDataToUpdate = cadastralDataFinder.getCadastralDataById(cadastralDataId);
        cadastralDataToUpdate.setPrice(request.getPrice());
        cadastralDataToUpdate.setRooms(request.getRooms());
        cadastralDataToUpdate.setFloor(request.getFloor());
        cadastralDataToUpdate.setEnergyClass(EnergyClass.valueOf(request.getEnergyClass().toUpperCase()));
        cadastralDataToUpdate.setSquareMeters(request.getSquareMeters());

        cadastralDataRepository.save(cadastralDataToUpdate);
    }
}
