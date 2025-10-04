package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.response.DetailResponse;
import com.dietiestates.api.serviceImpl.DetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class DetailController {

	private final DetailService detailService;

	@PostMapping
	public ResponseEntity<?> createDetail(
			@RequestBody DetailRequest request
				) {
		detailService.createDetail(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/{detailid}")
	public ResponseEntity<DetailResponse> getDetailById(
			@PathVariable Long detailid
			){
		var detail = detailService.getDetail(detailid);
		return ResponseEntity.status(HttpStatus.OK).body(detail);
	}
}
