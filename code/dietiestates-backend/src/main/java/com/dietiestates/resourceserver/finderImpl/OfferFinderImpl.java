package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.OfferNotFoundException;
import com.dietiestates.resourceserver.finder.OfferFinder;
import com.dietiestates.resourceserver.model.Offer;
import com.dietiestates.resourceserver.repository.OfferRepository;

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
