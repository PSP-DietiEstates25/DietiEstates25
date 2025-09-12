package com.dietiestates.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	
	public Optional<Search> getSearchById(Long id){
		return searchRepository.findById(id);
	}

}
