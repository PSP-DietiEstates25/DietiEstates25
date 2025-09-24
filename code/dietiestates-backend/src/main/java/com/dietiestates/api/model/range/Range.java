package com.dietiestates.api.model.range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Range<T extends Comparable<T>> {
	protected T min;
	protected T max;
}
