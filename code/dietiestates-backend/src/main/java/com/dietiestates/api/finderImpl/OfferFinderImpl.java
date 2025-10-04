package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.OfferNotFoundException;
import com.dietiestates.api.finder.OfferFinder;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.repository.OfferRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferFinderImpl implements OfferFinder {

	private final OfferRepository offerRepository;

	@Override
	public Offer getOfferById(Long id)
			throws OfferNotFoundException {
		return offerRepository.findById(id)
				.orElseThrow(OfferNotFoundException::new);
	}
	
}
