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
@Embeddable
public class PriceRange {

	@Column(nullable = false, name = "min_price")
	private BigDecimal minPrice;
	
	@Column(nullable = false, name = "max_price")
	private BigDecimal maxPrice;

    public boolean contains(BigDecimal price) {
        return price != null
                && minPrice != null
                && maxPrice != null
                && minPrice.compareTo(price) <= 0
                && price.compareTo(maxPrice) <= 0;
    }
}
