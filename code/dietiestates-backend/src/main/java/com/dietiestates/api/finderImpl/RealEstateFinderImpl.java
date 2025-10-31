package com.dietiestates.api.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;
import com.dietiestates.api.finder.RealEstateFinder;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.repository.RealEstateRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateFinderImpl implements RealEstateFinder {

	private final RealEstateRepository realEstateRepository;

	@Override
	public RealEstate getRealEstateById(Long id)
			throws RealEstateNotFoundException {
		return realEstateRepository.findById(id)
				.orElseThrow(RealEstateNotFoundException::new);
	}

	@Override
	public List<RealEstate> getAllRealEstates() {
		List<RealEstate> out = new ArrayList<>();
		realEstateRepository.findAll().forEach(out::add);
		return out;
	}

}
