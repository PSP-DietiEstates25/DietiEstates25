package com.dietiestates25.backend.model;

import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Embeddable
public class Coordinates {
	
	private String latitude;
	
	private String longitude;

}
