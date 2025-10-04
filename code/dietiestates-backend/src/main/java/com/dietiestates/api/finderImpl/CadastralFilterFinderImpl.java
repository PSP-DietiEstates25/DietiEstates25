package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.api.finder.CadastralFilterFinder;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.repository.CadastralFilterRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterFinderImpl implements CadastralFilterFinder {

	private final CadastralFilterRepository cadastralFilterRepository;

	@Override
	public CadastralFilter getCadastralFilterById(Long id)
			throws CadastralFilterNotFoundException {
		return cadastralFilterRepository.findById(id)
				.orElseThrow(CadastralFilterNotFoundException::new);
	}
}
