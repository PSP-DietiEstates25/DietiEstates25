package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.DetailNotFoundException;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.repository.DetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetailFinderDefaultImpl implements DetailFinder {

	private final DetailRepository detailRepository;

	@Override
	public Detail getDetailById(Long id)
			throws DetailNotFoundException {
		return detailRepository.findById(id)
				.orElseThrow(DetailNotFoundException::new);
	}
	
}
