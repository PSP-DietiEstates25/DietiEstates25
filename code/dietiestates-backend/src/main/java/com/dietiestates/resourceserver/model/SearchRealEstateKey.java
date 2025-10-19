package com.dietiestates.resourceserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
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
