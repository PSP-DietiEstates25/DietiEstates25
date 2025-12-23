package com.dietiestate.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.dietiestate.bff.config.BackendForFrontendServerProperties;

@SpringBootApplication
@EnableConfigurationProperties(BackendForFrontendServerProperties.class)
public class BackedForFrontendApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackedForFrontendApplication.class, args);
	}
}
