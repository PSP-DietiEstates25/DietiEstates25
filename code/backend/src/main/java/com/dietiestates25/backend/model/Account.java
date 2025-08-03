package com.dietiestates25.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable

public class Account {

	private String email;
	
	private String password;
}
