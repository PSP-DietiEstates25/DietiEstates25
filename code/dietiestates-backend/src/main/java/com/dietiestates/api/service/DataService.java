package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DataDto;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.DataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataService {

	private final DataRepository dataRepository;
	
	/*
	public CadastralData createData(DataDto request) {
		
	}*/
}
