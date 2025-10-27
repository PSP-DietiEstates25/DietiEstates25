package com.dietiestates.resource_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

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
