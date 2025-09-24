package com.dietiestates.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SearchRealEstateKey {

	@Column(nullable = false, name = "real_estate_id")
	private Long realEstateId;
	
	@Column(nullable = false, name = "search_id")
	private Long searchId;
}
