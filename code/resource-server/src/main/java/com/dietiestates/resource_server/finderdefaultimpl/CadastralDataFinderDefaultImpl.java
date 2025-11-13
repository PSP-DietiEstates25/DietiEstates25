package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resource_server.finder.CadastralDataFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.repository.CadastralDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralDataFinderDefaultImpl implements CadastralDataFinder {

	private final CadastralDataRepository cadastralDataRepository;
    private final RealEstateFinder realEstateFinder;
	
	@Override
	public CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException {
		return cadastralDataRepository.findById(id)
				.orElseThrow(CadastralDataNotFoundException::new);
	}

    @Override
    public CadastralData getRealEstateCadastralData(Long realEstateId) throws CadastralDataNotFoundException {
        var realEstate = realEstateFinder.getRealEstateById(realEstateId);
        return cadastralDataRepository.findById(realEstate.getCadastralData().getId())
                .orElseThrow(CadastralDataNotFoundException::new);
    }
}