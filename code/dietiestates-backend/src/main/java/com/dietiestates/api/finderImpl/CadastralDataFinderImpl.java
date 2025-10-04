package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.api.finder.CadastralDataFinder;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.CadastralDataRepository;

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
