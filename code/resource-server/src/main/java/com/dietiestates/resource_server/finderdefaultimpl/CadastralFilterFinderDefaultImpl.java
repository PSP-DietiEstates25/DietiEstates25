package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resource_server.finder.CadastralFilterFinder;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.repository.CadastralFilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastralFilterFinderDefaultImpl implements CadastralFilterFinder {

	private final CadastralFilterRepository cadastralFilterRepository;

	@Override
	public CadastralFilter getCadastralFilterById(Long id)
			throws CadastralFilterNotFoundException {
		return cadastralFilterRepository.findById(id)
				.orElseThrow(CadastralFilterNotFoundException::new);
	}

    @Override
    public CadastralFilter getSearchCadastralFilter(Long searchId) throws CadastralFilterNotFoundException {
        return cadastralFilterRepository.findBySearchId(searchId)
                .orElseThrow(CadastralFilterNotFoundException::new);
    }
}
