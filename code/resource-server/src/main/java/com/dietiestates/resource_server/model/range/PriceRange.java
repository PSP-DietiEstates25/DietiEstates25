package com.dietiestates.resource_server.model.range;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode
@Embeddable
public class PriceRange {

	@Column(nullable = false, name = "min_price")
	private BigDecimal minPrice;
	
	@Column(nullable = false, name = "max_price")
	private BigDecimal maxPrice;
	
	public Boolean contains(BigDecimal price) {
		
		if(price != null && this.minPrice != null && this.maxPrice != null) {
			
			if(this.minPrice.compareTo(price) <= 0 && price.compareTo(this.maxPrice) <= 0)
				return true;
			else return false;
		
		} else return false;
	}
}
