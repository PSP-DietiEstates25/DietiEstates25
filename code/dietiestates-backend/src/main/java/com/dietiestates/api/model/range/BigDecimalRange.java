package com.dietiestates.api.model.range;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class BigDecimalRange extends Range<BigDecimal>{

}
