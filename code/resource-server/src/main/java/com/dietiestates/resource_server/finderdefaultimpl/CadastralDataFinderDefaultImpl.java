package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resource_server.finder.CadastralDataFinder;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.repository.CadastralDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralDataFinderDefaultImpl implements CadastralDataFinder {

	private final CadastralDataRepository cadastralDataRepository;
	
	@Override
	public CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException {
		return cadastralDataRepository.findById(id)
				.orElseThrow(CadastralDataNotFoundException::new);
	}

    @Override
    public CadastralData getRealEstateCadastralData(Long realEstateId) throws CadastralDataNotFoundException {
        return cadastralDataRepository.findByRealEstateId(realEstateId)
                .orElseThrow(CadastralDataNotFoundException::new);
    }
}
