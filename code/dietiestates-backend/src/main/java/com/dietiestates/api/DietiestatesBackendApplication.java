package com.dietiestates.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DietiestatesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DietiestatesBackendApplication.class, args);
	}
}
