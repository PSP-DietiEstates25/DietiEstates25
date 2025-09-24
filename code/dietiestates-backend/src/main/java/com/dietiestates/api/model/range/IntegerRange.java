package com.dietiestates.api.model.range;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class IntegerRange{
	
	private Integer min;
	
	private Integer max;
}
