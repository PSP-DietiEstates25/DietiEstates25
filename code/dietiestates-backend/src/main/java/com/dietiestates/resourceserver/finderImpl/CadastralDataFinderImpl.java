package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resourceserver.finder.CadastralDataFinder;
import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.repository.CadastralDataRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataFinderImpl implements CadastralDataFinder {

	private final CadastralDataRepository cadastralDataRepository;
	
	@Override
	public CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException {
		return cadastralDataRepository.findById(id)
				.orElseThrow(CadastralDataNotFoundException::new);
	}

}
