package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resourceserver.finder.CadastralFilterFinder;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.repository.CadastralFilterRepository;

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
